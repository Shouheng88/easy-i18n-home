package me.shouheng.i18n.i18n.flutter

import io.github.vinceglb.filekit.*
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.i18n.IKeywordLoader

/** Flutter .arb 加载器 */
class FlutterKeyLoader: IKeywordLoader {

    override fun load(path: I18nPath): List<I18nWordModel> {
        val directory = PlatformFile(path.path)
        if (!directory.isDirectory() || !directory.exists()) {
            return emptyList()
        }

        val resourcesList = mutableListOf<FileResources>()
        directory.list().sortedBy { it.name }.forEach { file ->
            if (file.name.endsWith(".arb")) {
                val resources = travelArbFile(file)
                resourcesList.add(resources)
            }
        }

        val names = mutableListOf<String>()
        resourcesList.forEach { resources ->
            resources.resources.forEach { resource ->
                val name = resource.getTextName()
                if (!names.contains(name)) {
                    names.add(name)
                }
            }
        }

        val wordDTOs = DB.i18nWordDao.getOf(path.path)
        val wordDTOMap = mutableMapOf<String, I18nWord>()
        wordDTOs.forEach { wordDTOMap[it.name] = it }

        val languageFileResourcesMap = mutableMapOf<String, FileResources>()
        resourcesList.forEach { languageFileResourcesMap[it.language] = it }

        // 对每个词条进行处理，获取它在各个语言下的信息
        val words = mutableListOf<I18nWordModel>()
        names.forEachIndexed { index, name ->
            val meanings = mutableListOf<I18nWordModel.Meaning>()
            languageFileResourcesMap.entries.forEach { entry ->
                val language = entry.key
                val fileResources = entry.value
                val resource = fileResources.getTextResource(name)
                meanings.add(I18nWordModel.Meaning.from(language, fileResources.file, resource))
            }
            // 虽然 flutter 的词条上面有备注信息，但是不展示它了，因为这个备注信息每个语言的词条有一个，而我们的设计是
            // 一个词条一个，因此，更新的话容易引起歧义。因此，这里的备注只展示数据库的备注
            val wordDTO = wordDTOMap[name]
            words.add(I18nWordModel.ofArb(name, meanings, wordDTO?.description, wordDTO, path, index))
        }

        return words
    }

    /** 对 arb 文件进行解析 */
    private fun travelArbFile(file: PlatformFile): FileResources {
        val name = file.name.removeSuffix(".arb")
        val index = name.indexOf('_')
        val language = if (index >= 0) name.substring(index+1) else ""
        val resources = FlutterArbParser.parse(file)
        return FileResources(language, file, resources)
    }

    /** 单个 properties 文件的信息 */
    private data class FileResources(
        val language: String,
        val file: PlatformFile,
        val resources: List<AbsTextResource>,
    ) {

        /** 构建一个 “name-资源” 映射关系 */
        private val resourcesMap = mutableMapOf<String, AbsTextResource>()

        init {
            resources.forEach {
                resourcesMap[it.getTextName()] = it
            }
        }

        /** 获取多语言词条 */
        fun getTextResource(name: String): AbsTextResource? {
            return resourcesMap[name]
        }
    }
}