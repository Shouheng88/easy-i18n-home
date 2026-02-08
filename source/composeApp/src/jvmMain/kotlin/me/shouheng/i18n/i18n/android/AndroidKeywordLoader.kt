package me.shouheng.i18n.i18n.android

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.i18n.IKeywordLoader
import me.shouheng.i18n.utils.extension.logd
import kotlin.collections.forEach

/** Android 多语言文件加载 */
class AndroidKeywordLoader: IKeywordLoader {

    override fun load(path: I18nPath): List<I18nWordModel> {
        val directory = PlatformFile(path.path)
        if (!directory.isDirectory() || !directory.exists()) {
            return emptyList()
        }

        val resourcesList = mutableListOf<FileResources>()
        val directories = mutableListOf<PlatformFile>()
        directories.add(directory)
        while (directories.isNotEmpty()) {
            val directory = directories.removeAt(0)
            if (directory.isDirectory()) {
                if (directory.name.startsWith("values")) {
                    val resources = travelValuesDirectory(directory) ?: continue
                    resourcesList.add(resources)
                } else {
                    directories.addAll(directory.list().sortedBy { it.name })
                }
            }
        }

        // 当前路径下所有多语言词条的 name 的并集：注意顺序要和源文件中的顺序一致
        val names = mutableListOf<String>()
        resourcesList.forEach { resources ->
            resources.resources.forEach { resource ->
                val name = resource.getTextName()
                if (!names.contains(name)) {
                    names.add(name)
                }
            }
        }

        // 数据库中的信息
        val wordDTOs = DB.i18nWordDao.getOf(path.path)
        val wordDTOMap = mutableMapOf<String, I18nWord>()
        wordDTOs.forEach { wordDTOMap[it.name] = it }

        // 构建多语言到它的文件信息的映射关系
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
            val wordDTO = wordDTOMap[name]
            words.add(I18nWordModel.Companion.ofAndroid(name, meanings, wordDTO?.description, wordDTO, path, index))
        }

        return words
    }

    /** 对 values 目录进行遍历 */
    private fun travelValuesDirectory(directory: PlatformFile): FileResources? {
        val language = directory.name
            .removePrefix("values")
            .removePrefix("-") // 文件名中的多语言，如 "en"、"en-rUS"
        val stringsFile = directory.list().firstOrNull {
            it.name == "strings.xml"
        }
        stringsFile ?: logd { "Didn't find strings.xml file under ${directory.path}" }
        stringsFile ?: return null

        val resources = AndroidXmlParser.parse(stringsFile).texts

        return FileResources(language, stringsFile, resources)
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
    }
}