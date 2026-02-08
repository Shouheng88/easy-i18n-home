package me.shouheng.i18n.i18n.ios

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.isRegularFile
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.i18n.IKeywordLoader
import kotlin.collections.forEach

/** iOS 多语言加载 */
class IOSKeywordLoader: IKeywordLoader {

    override fun load(path: I18nPath): List<I18nWordModel> {
        val directory = PlatformFile(path.path)
        if (!directory.isDirectory() || !directory.exists()) {
            return emptyList()
        }

        val type = I18nResourceType.from(path.resourceType.toInt())
        return when (type) {
            I18nResourceType.IOSCStrings -> loadDotStringWords(path, directory)
            I18nResourceType.IOSXCStrings -> loadXCStringWords(path, directory)
            else -> emptyList()
        }
    }

    /** 加载 .strings 格式的多语言词条 */
    private fun loadDotStringWords(path: I18nPath, directory: PlatformFile): List<I18nWordModel> {
        val resourcesList = mutableListOf<FileResources>()
        directory.list().sortedBy { it.name }.forEach { child ->
            if (child.name.endsWith(".lproj")) {
                // .strings 文件加载
                val resources = travelLprojDirectory(child)
                resourcesList.addAll(resources)
            }
        }

        // 当前路径下所有多语言词条的 name 的并集：注意顺序要和源文件中的顺序一致
        val names = mutableListOf<String>()
        val nameToFileName = mutableMapOf<String, String>()
        resourcesList.forEach { resources ->
            resources.resources.forEach { resource ->
                val name = resource.getTextName()
                if (!names.contains(name)) {
                    names.add(name)
                }
                nameToFileName[name] = resource.file.name
            }
        }

        // 数据库中的信息
        val wordDTOs = DB.i18nWordDao.getOf(path.path)
        val wordDTOMap = mutableMapOf<String, I18nWord>()
        wordDTOs.forEach { wordDTOMap[it.name] = it }

        // 构建多语言到它的文件信息的映射关系
        val languageFileResourcesMap = mutableMapOf<String, List<FileResources>>()
        resourcesList.forEach {
            val list = (languageFileResourcesMap[it.language]?.toMutableList() ?: mutableListOf())
            list.add(it)
            languageFileResourcesMap[it.language] = list
        }

        // 对每个词条进行处理，获取它在各个语言下的信息
        val words = mutableListOf<I18nWordModel>()
        names.forEachIndexed { index, name ->
            val meanings = mutableListOf<I18nWordModel.Meaning>()
            languageFileResourcesMap.entries.forEach { entry ->
                val language = entry.key
                val fileResources = entry.value
                // 由于一个语言可能对应多个文件，因此先找这个关键字在该语言下所在的文件
                var fileResource = fileResources.firstOrNull { it.contains(name) }
                // 该词条可能在该语言下没有翻译，因此，按照文件名的规则去找该文件在该语言下对应的文件
                if (fileResource == null) {
                    val fileName = nameToFileName[name]
                    fileResource = fileResources.firstOrNull { it.file.name == fileName }
                }
                // 仍然找到，就用该语言的第一个文件
                if (fileResource == null) {
                    fileResource = fileResources.firstOrNull()
                }
                // 该语言下没有文件（一般来说不存在这种情况），那么就不管了...
                if (fileResource == null) {
                    return@forEach
                }
                val resource = fileResource.getTextResource(name)
                meanings.add(I18nWordModel.Meaning.from(language, fileResource.file, resource))
            }
            val wordDTO = wordDTOMap[name]
            words.add(I18nWordModel.Companion.ofDotString(name, meanings, wordDTO?.description, wordDTO, path, index))
        }

        return words
    }

    /** 加载 .xcstrings 格式的多语言词条 */
    private fun loadXCStringWords(path: I18nPath, directory: PlatformFile): List<I18nWordModel> {
        val i18nWords = mutableListOf<I18nWordModel>()

        val fileMap = mutableMapOf<String, PlatformFile>()
        val xcStringResources = mutableListOf<XCStringResource>()
        directory.list().sortedBy { it.name }.forEach { child ->
            if (child.name.endsWith(".xcstrings")) {
                val resources = travelXCStringsFile(child)
                resources.forEach { resource ->
                    fileMap[resource.name] = child
                }
                xcStringResources.addAll(resources)
            }
        }

        // 数据库中的信息
        val wordDTOs = DB.i18nWordDao.getOf(path.path)
        val wordDTOMap = mutableMapOf<String, I18nWord>()
        wordDTOs.forEach { wordDTOMap[it.name] = it }

        // 单词名称-资源 的映射关系
        val resourceMap = mutableMapOf<String, XCStringResource>()
        xcStringResources.forEach { resourceMap[it.name] = it }

        // 同一个目录下的 .xcstrings 文件要做统一处理，保证语言顺序等一致，否则表头部分会出问题
        val names = mutableListOf<String>()
        val nameMap = mutableMapOf<String, Map<String, XCStringResource>>()
        xcStringResources.forEach { resource ->
            if (!names.contains(resource.name)) {
                names.add(resource.name)
            }
            nameMap[resource.name] = (nameMap[resource.name]?.toMutableMap() ?: mutableMapOf()).also {
                it[resource.language] = resource
            }
        }

        val languages = mutableListOf<String>()
        xcStringResources.forEach { resource ->
            if (!languages.contains(resource.language)) {
                languages.add(resource.language)
            }
        }

        names.forEachIndexed { index, name ->
            val meanings = mutableListOf<I18nWordModel.Meaning>()
            languages.forEach { language ->
                val resource = nameMap[name]?.get(language)
                meanings.add(I18nWordModel.Meaning.from(language, fileMap[name]!!, resource,))
            }
            val resource = resourceMap[name]
            val wordDTO = wordDTOMap[name]
            val description = resource?.getDescription() ?: wordDTO?.description
            i18nWords.add(I18nWordModel.Companion.ofXCString(name, meanings, description, wordDTO, path, index, resource!!))
        }

        return i18nWords
    }

    /** .strings 文件的遍历 */
    private fun travelLprojDirectory(directory: PlatformFile): List<FileResources> {
        val fileResources = mutableListOf<FileResources>()
        val language = directory.name.removeSuffix(".lproj")

        // 可能存在一个目录下多个文件
        directory.list().sortedBy { it.name }.forEach { file ->
            if (file.isRegularFile() && file.exists() && file.name.endsWith(".strings")) {
                val resources = DotStringsParser.parse(file)
                fileResources.add(FileResources(language, file, resources))
            }
        }

        return fileResources
    }

    /** 访问 .xcstrings 文件 */
    private fun travelXCStringsFile(file: PlatformFile): List<XCStringResource> {
        if (!file.isRegularFile() || !file.name.endsWith(".xcstrings")) {
            return emptyList()
        }

        return XCStringsParser.parse(file)
    }

    /** 单个 strings.xml 文件的信息 */
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

        /** 判断是否包含指定的 key */
        fun contains(name: String): Boolean = resourcesMap.contains(name)
    }
}