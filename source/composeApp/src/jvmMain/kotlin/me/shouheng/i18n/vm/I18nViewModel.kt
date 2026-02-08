package me.shouheng.i18n.vm

import androidx.lifecycle.viewModelScope
import easy_i18n.composeapp.generated.resources.*
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.data.model.*
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.manager.*
import org.jetbrains.compose.resources.getString

/** 多语言工具的 ViewModel */
class I18nViewModel: BaseViewModel() {

    private val wordQuery = WordQuery()

    /** 初次加载的状态：初次记载的时候有加载态，后续的没有 */
    val firstLoading = MutableStateFlow<Boolean?>(null)
    val projects = MutableStateFlow<List<I18nProject>>(emptyList())
    val project = MutableStateFlow<I18nProject?>(null)
    val paths = MutableStateFlow<List<I18nPath>>(emptyList())
    val platform = MutableStateFlow(I18nPlatform.Android)
    val pathContent = MutableStateFlow<PathContent?>(null)
    val translateState = MutableStateFlow<AutoTranslateState?>(null)
    val wordTranslateState = MutableStateFlow<AutoTranslateState?>(null)
    val wordUpdateState = MutableStateFlow<Resource<I18nWordModel>?>(null)
    val detectedDirectories = MutableStateFlow<List<PathDetected>>(emptyList())
    val ignoreLanguages = MutableStateFlow<List<String>>(emptyList())

    /** 加载项目 */
    fun loadProjects() {
        if (firstLoading.value == null) {
            firstLoading.value = true
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nProjectDao.getAll()
                }
            }.onSuccess { projects ->
                // 先根据设置找，找不到就用第一个作为当前使用中的项目
                val project = projects.firstOrNull { it.id == I18nManager.getWorkingProjectId() } ?: projects.firstOrNull()
                // 设置平台
                platform.value = I18nManager.getWorkingPlatformOf(project)
                // 加载项目的路径
                if (project != null) {
                    loadPaths(project)
                } else {
                    firstLoading.value = false
                }
                this@I18nViewModel.project.value = project
                this@I18nViewModel.projects.value = projects
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /** 设置使用中的项目 */
    fun setWorkingProject(project: I18nProject) {
        if (this.project.value?.id == project.id) {
            return
        }
        detectedDirectories.value = emptyList()
        this@I18nViewModel.project.value = project
        I18nManager.setWorkingProject(project)
        loadPaths(project)
    }

    /** 使用中的平台 */
    fun setWorkingPlatform(platform: I18nPlatform) {
        if (this.platform.value.id == platform.id) {
            return
        }
        val project = this.project.value ?: return
        detectedDirectories.value = emptyList()
        this.platform.value = platform
        I18nManager.setWorkingPlatform(project, platform)
        loadPaths(project)
    }

    /** 创建项目 */
    fun createProject(name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nProjectDao.insert(name, description = description)!!
                }
            }.onSuccess {
                setWorkingProject(it)
                loadProjects()
                showSuccess { getString(Res.string.project_create_success, name) }
            }.onFailure {
                it.printStackTrace()
                showError { getString(Res.string.project_create_failed, name, it.message ?: "") }
            }
        }
    }

    /** 删除项目 */
    fun deleteProject(project: I18nProject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nProjectDao.delete(project.id)
                }
            }.onSuccess {
                loadProjects()
                showSuccess { getString(Res.string.project_delete_success, project.name) }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /** 编辑项目 */
    fun editProject(project: I18nProject, name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nProjectDao.update(project, name = name, description = description)
                }
            }.onSuccess {
                loadProjects()
                showSuccess { getString(Res.string.project_update_success, name) }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /** 存储多语言文件路径 */
    fun savePaths(directories: List<PathDetected>) {
        val project = project.value ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    directories.forEach {
                        DB.i18nPathDao.insert(project.id, platform.value, it.file.absolutePath(), it.type)
                    }
                }
            }.onSuccess {
                loadPaths(project)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /** 刷新单词列表：当前路径的单词列表 */
    fun reloadWords() {
        val path = pathContent.value?.path ?: return
        loadWords(path)
    }

    /** 加载指定路径的词条资源 */
    fun loadWords(path: I18nPath) {
        I18nManager.setWorkingPath(project.value, platform.value, path)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    WordManager.loadWords(path, wordQuery)
                }
            }.onSuccess {
                pathContent.value = it
                firstLoading.value = false
            }.onFailure {
                it.printStackTrace()
                firstLoading.value = false
            }
        }
    }

    /** 删除词条 */
    fun deleteWord(word: I18nWordModel) {
        WordManager.deleteWord(word) { res ->
            if (res.isSuccess) {
                showSuccess { getString(Res.string.word_delete_success, word.name) }
                // 刷新单词列表
                reloadWords()
            } else if (res.isFailure) {
                res.throwable?.printStackTrace()
                showError { getString(Res.string.word_delete_failed, word.name, "${res.code}:${res.message}") }
            }
        }
    }

    /** 更新词条的含义和描述 */
    fun updateWord(word: I18nWordModel, items: List<I18nDialogEditItem>, description: String) {
        WordManager.updateWord(word, items, description) { res ->
            if (res.isSuccess) {
                showSuccess { getString(Res.string.word_update_success, word.name) }
                // 刷新单词列表
                reloadWords()
            } else if (res.isFailure) {
                res.throwable?.printStackTrace()
            }
            // 单词更新的结果
            wordUpdateState.value = res
        }
    }

    /** 自动翻译 */
    fun autoTranslate(language: I18nLanguage) {
        if (isTranslatorRunning()) {
            showError(Res.string.translate_auto_translating)
            return
        }
        val content = pathContent.value
        content ?: showError(Res.string.translate_state_abnormal)
        content ?: return
        val project = project.value
        project ?: showError(Res.string.translate_state_abnormal)
        project ?: return

        // 下面的代码等价于 <=>
        WordManager.autoTranslate(project, content, language) { res ->
            // 需要 copy 一下，否则 flow 判断有问题
            translateState.value = res.copy()
            // 刷新单词列表
            if (res.canReload) {
                reloadWords()
            }
        }
    }

    /** 自动翻译 */
    fun autoTranslate(
        description: String,
        editItems: List<I18nDialogEditItem>,
        onItemsChange: (List<I18nDialogEditItem>) -> Unit,
    ) {
        if (isTranslatorRunning()) {
            showError(Res.string.translate_auto_translating)
            return
        }
        val content = pathContent.value
        content ?: showError(Res.string.translate_state_abnormal)
        content ?: return
        val project = project.value
        project ?: showError(Res.string.translate_state_abnormal)
        project ?: return

        // 下面的代码等价于 <=>
        WordManager.autoTranslateWord(
            project,
            content,
            description,
            editItems
        ) { res, items, changed ->
            wordTranslateState.value = res.copy()
            if (changed) {
                onItemsChange(items)
            }
        }
    }

    /** 更新指定路径的翻译的基准语言 */
    fun updateSourceLanguage(path: PathContent?, language: I18nLanguage) {
        val path = path?.path ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nPathDao.update(path.id, language.ios.firstOrNull() ?: "")
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    /** 是否正在翻译中 */
    fun isTranslatorRunning(): Boolean = translateState.value?.isRunning() == true

    /** 清空单词的翻译状态 */
    fun clearWordTranslateState() {
        wordTranslateState.value = null
    }

    /** 扫描指定的目录 */
    fun detectDirectories(file: PlatformFile) {
        val matched = I18nManager.detect(file, platform.value)
        if (matched.isEmpty()) {
            showWarn(Res.string.folder_not_found)
            return
        }
        val directories = detectedDirectories.value.toMutableList()
        directories.addAll(matched)
        detectedDirectories.value = directories
    }

    /** 移除指定的目录 */
    fun removeDetectedDirectory(detected: PathDetected) {
        val directories = detectedDirectories.value.toMutableList()
        directories.remove(detected)
        detectedDirectories.value = directories
    }

    /** 更新多语言路径 */
    fun changePaths(directories: List<PathDetected>, toDelete: List<PathDetected>) {
        val project = project.value ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    // 增加 新增的路径
                    directories.forEach {
                        if (it.path == null) {
                            DB.i18nPathDao.insert(project.id, platform.value, it.file.absolutePath(), it.type)
                        }
                    }
                    // 删除
                    toDelete.forEach {
                        val path = it.path ?: return@forEach
                        DB.i18nPathDao.delete(path.id)
                    }
                }
            }.onFailure {
                showError { getString(Res.string.text_save_failed_with, it.message ?: "") }
            }.onSuccess {
                // 重新加载路径
                loadPaths(project)
                showSuccess(Res.string.text_save_succeed)
            }
        }
    }

    /** 更新 encoding */
    fun updateEncoding(path: I18nPath, encoding: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nPathDao.updateEncoding(path.id, encoding)
                }
            }.onSuccess {
                project.value?.let {
                    loadPaths(it)
                }
            }
        }
    }

    /** 修改翻译比的顺序 */
    fun nextRateOrder() {
        wordQuery.nextRateOrder()
        reloadWords()
    }

    /** 修改更新时间的顺序 */
    fun nextUpdatedOrder() {
        wordQuery.nextUpdatedOrder()
        reloadWords()
    }

    /** 修改名称的顺序 */
    fun nextNameOrder() {
        wordQuery.nextNameOrder()
        reloadWords()
    }

    /** 搜索指定的关键字 */
    fun search(keyword: String?) {
        wordQuery.ofKeyword(keyword)
        reloadWords()
    }

    /** 加载项目的路径 */
    private fun loadPaths(project: I18nProject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    DB.i18nPathDao.getOf(project.id, platform.value)
                }
            }.onSuccess { i18Paths ->
                paths.value = i18Paths
                if (i18Paths.isNotEmpty()) {
                    val working = I18nManager.getWorkingPath(project, platform.value)
                    val path = i18Paths.firstOrNull { it.path == working } ?: i18Paths.firstOrNull()
                    loadWords(path!!)
                } else {
                    firstLoading.value = false
                }
            }.onFailure {
                it.printStackTrace()
                firstLoading.value = false
            }
        }
    }
}