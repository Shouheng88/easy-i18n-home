package me.shouheng.i18n.manager

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.translate_standard_miss
import kotlinx.coroutines.*
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.data.ErrorCode
import me.shouheng.i18n.data.ErrorCode.TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.*
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.utils.extension.logd
import me.shouheng.i18n.utils.extension.loge
import org.jetbrains.compose.resources.getString

/** 多语言-单词管理 */
object WordManager {

    private const val MAX_ERROR_COUNT = 5

    /** 加载多语言词条 */
    fun loadWords(
        path: I18nPath,
        query: WordQuery,
    ): PathContent {
        val loader = I18nPlatform.from(path.platform.toInt()).newLoader()
        var words = loader.load(path)
        words = query.handle(words)
        return PathContent(path, words, query)
    }

    /** 删除词条 */
    fun deleteWord(word: I18nWordModel, callback: (Resource<I18nWordModel>) -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    word.deleter.delete(word)
                }
            }.onSuccess {
                if (it.isSuccess) {
                    callback(Resource.success(word))
                } else {
                    callback(Resource.failure(it))
                }
            }.onFailure {
                callback(Resource.failure("", it.message, throwable = it.cause))
            }
        }
    }

    /** 更新词条的含义和描述 */
    fun updateWord(
        word: I18nWordModel,
        items: List<I18nDialogEditItem>,
        description: String,
        callback: (Resource<I18nWordModel>) -> Unit
    ) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    val updateItems = items.map { it.toWordUpdateItem() }
                    word.updater.update(word, updateItems, description).apply {
                        if (isSuccess) {
                            DB.i18nWordDao.markUpdated(word)
                        }
                    }
                }
            }.onSuccess {
                if (it.isSuccess) {
                    callback(Resource.success(word))
                } else {
                    callback(Resource.failure(it))
                }
            }.onFailure {
                callback(Resource.failure("", it.message, throwable = it.cause))
            }
        }
    }

    /** 自动翻译 */
    @OptIn(DelicateCoroutinesApi::class)
    fun autoTranslateWord(
        project: I18nProject,
        content: PathContent,
        description: String,
        editItems: List<I18nDialogEditItem>,
        callback: (AutoTranslateState, List<I18nDialogEditItem>, Boolean) -> Unit
    ) {
        var items = editItems
        val state = AutoTranslateState(AutoTranslateState.Status.RUNNING)
        state.total = items.count { it.value.isEmpty() }
        val appInfo = project.description
        val resourceType = I18nResourceType.from(content.path.resourceType.toInt())

        // 查找第一个词条
        val standard = editItems.firstOrNull()?.value
        if (standard.isNullOrEmpty()) {
            GlobalScope.launch {
                state.status = AutoTranslateState.Status.ERROR
                state.errorCode = "-1"
                state.errorMessage = getString(Res.string.translate_standard_miss)
                callback(state, items, false)
            }
            return
        }

        // 开始
        callback(state, items, false)

        // 基于第一个词条进行翻译
        val delay = TranslatorManager.getTranslateDelay()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                editItems.forEachIndexed { index, item ->
                    if (item.value.isEmpty()) {
                        // 延时一会儿
                        delay(delay)

                        // 翻译
                        val res = TranslatorManager.translate(
                            resourceType,
                            standard,
                            item.meaning.language,
                            description,
                            appInfo ?: ""
                        )
                        state.requestCount += 1

                        // 处理翻译结果
                        val text = res.data
                        if (res.isFailure || text.isNullOrEmpty()) {
                            // 翻译失败
                            loge { "为[${item.meaning.language}]翻译，失败：$state" }
                            if (res.code == ErrorCode.TRANSLATOR_TARGET_LANGUAGE_DEFAULT
                                || res.code == TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND) {
                                state.skippedCount += 1
                            } else {
                                state.errorCode = res.code
                                state.errorMessage = res.message
                                state.errorCount += 1
                            }
                            withContext(Dispatchers.Main) {
                                callback(state, items, false)
                            }
                        } else if (res.isSuccess) {
                            // 翻译成功
                            state.translatedCount += 1
                            items = items.toMutableList().also {
                                it[index] = it[index].copy(value = text)
                            }
                            withContext(Dispatchers.Main) {
                                callback(state, items, true)
                            }
                        }
                    }
                }

                // 翻译完成
                state.status = if (state.errorCount != 0) AutoTranslateState.Status.ERROR
                    else AutoTranslateState.Status.COMPLETED
                withContext(Dispatchers.Main) {
                    callback(state, items, false)
                }
            }
        }
    }

    /** 自动翻译 */
    fun autoTranslate(
        project: I18nProject,
        content: PathContent,
        language: I18nLanguage,
        callback: (AutoTranslateState) -> Unit
    ) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    val state = AutoTranslateState(AutoTranslateState.Status.RUNNING)
                    val words = content.words
                    val toTranslate = words.filter { it.isNeedTranslate() }
                    state.total = content.countWordsNeedTranslate()

                    // 待翻译的内容为空，直接返回
                    if (toTranslate.isEmpty()) {
                        state.status = AutoTranslateState.Status.COMPLETED
                        withContext(Dispatchers.Main) {
                            callback(state)
                        }
                        return@runCatching
                    }

                    // 开始翻译
                    withContext(Dispatchers.Main) {
                        callback(state)
                    }
                    val list = mutableListOf<Pair<I18nWordModel, Int>>()
                    toTranslate.forEach {
                        var index = words.indexOf(it)
                        if (index < 0) {
                            index = words.size
                        }
                        list.add(it to index)
                    }
                    val resourceType = I18nResourceType.from(content.path.resourceType.toInt())
                    doAutoTranslate(resourceType, list, state, language, project.description, callback)
                }
            }
        }
    }

    /**
     * 执行翻译
     *
     * @param language 基准语言：外部传入，以此为基准进行翻译
     */
    private suspend fun doAutoTranslate(
        resourceType: I18nResourceType,
        list: List<Pair<I18nWordModel, Int>>,
        state: AutoTranslateState,
        language: I18nLanguage,
        appInfo: String?,
        callback: (AutoTranslateState) -> Unit,
    ) {
        val delay = TranslatorManager.getTranslateDelay()

        // 对单个单词进行翻译
        list.forEach { item ->
            val word = item.first
            val index = item.second

            // 优先使用单词本身自带的多语言
            var fromLanguage = language
            val sourceLanguage = word.getSourceLanguage()
            if (!sourceLanguage.isNullOrEmpty()) {
                fromLanguage = LanguageManager.getLanguage(sourceLanguage, resourceType) ?: language
            }

            // 寻找原始含义
            val meaning = word.meanings.firstOrNull { fromLanguage.contains(it.language) }
            // 找不到对应的含义，跳过
            if (meaning == null || meaning.origin == null) {
                state.skippedCount += 1
                logd { "翻译[${word.name}]跳过：$state" }
                withContext(Dispatchers.Main) {
                    callback(state)
                }
                return@forEach
            }

            // 对各个含义进行翻译
            val items = mutableListOf<WordUpdateItem>()
            val toTranslate = word.meanings.filter { it.isNeedTranslate() }
            // 原始的含义也塞进去：因为需要通过其他数据判断其数据类型
            items.addAll(word.meanings.filter { !it.isNeedTranslate() }.map {
                WordUpdateItem(it.origin!!.getDisplayValue(), it, false)
            })
            toTranslate.forEach {

                // 延时一会儿
                delay(delay)

                // 翻译
                val res = TranslatorManager.translate(
                    resourceType,
                    meaning.origin.getDisplayValue(),
                    it.language,
                    word.description ?: "",
                    appInfo ?: ""
                )
                state.requestCount += 1

                // 结果
                val text = res.data
                if (res.isFailure || text == null) {
                    loge { "翻译[${word.name}]为[${it.language}]失败：$state" }
                    if (res.code == ErrorCode.TRANSLATOR_TARGET_LANGUAGE_DEFAULT
                        || res.code == TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND) {
                        // 如果是因为默认语言或者不支持该语言翻译失败，不计入失败次数
                        state.skippedCount += 1
                    } else {
                        state.errorCount += 1
                        if (state.errorCount >= MAX_ERROR_COUNT) {
                            // 达到指定错误次数
                            state.status = AutoTranslateState.Status.ERROR
                            state.errorCode = ErrorCode.TRANSLATE_ERROR_MAX_ERROR_COUNT
                            state.errorMessage = res.message
                            withContext(Dispatchers.Main) {
                                callback(state)
                            }
                            return
                        }
                    }
                    withContext(Dispatchers.Main) {
                        callback(state)
                    }
                }
                items.add(WordUpdateItem(text!!, it, true))

                // 更新状态
                state.translatedCount += 1
                logd { "翻译[${word.name}]为[${it.language}]完成：$state" }
                withContext(Dispatchers.Main) {
                    callback(state)
                }
            }

            // 更新到文件中
            val res = word.updater.update(word,  items, word.description ?: "")
            if (res.isSuccess) {
                logd { "翻译[${word.name}]完成: $state" }
                state.canReload = true
                withContext(Dispatchers.Main) {
                    callback(state)
                }
                state.canReload = false
                DB.i18nWordDao.markUpdated(word)
            } else if (res.isFailure) {
                state.status = AutoTranslateState.Status.ERROR
                state.errorCode = res.code
                state.errorMessage = res.message
                withContext(Dispatchers.Main) {
                    callback(state)
                }
                return
            }
        }

        // 翻译完成的回调
        state.status = AutoTranslateState.Status.COMPLETED
        withContext(Dispatchers.Main) {
            callback(state)
        }
    }
}