package me.shouheng.i18n.i18n.json

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.*
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.db.DB
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.i18n.IKeywordLoader

class JsonKeyLoader : IKeywordLoader {

    override fun load(path: I18nPath): List<I18nWordModel> {
        val directory = PlatformFile(path.path)
        if (!directory.isDirectory() || !directory.exists()) {
            return emptyList()
        }

        val resourcesList = mutableListOf<FileResources>()
        directory.list().sortedBy { it.name }.forEach { file ->
            if (file.name.endsWith(".json")) {
                val resources = travelJsonFile(file)
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
            words.add(I18nWordModel.ofJson(name, meanings, wordDTO?.description, wordDTO, path, index))
        }

        return words
    }

    private fun travelJsonFile(file: PlatformFile): FileResources {
        val name = file.name.removeSuffix(".json")
        // Strategy: Use filename as language code. 
        // If it contains underscore, take the part after the last underscore? 
        // Or just use the whole name. 
        // For Vue i18n, usually en.json, zh-CN.json.
        val language = name 
        val resources = JsonParser.parse(file)
        return FileResources(language, file, resources)
    }

    private data class FileResources(
        val language: String,
        val file: PlatformFile,
        val resources: List<AbsTextResource>,
    ) {
        private val resourcesMap = mutableMapOf<String, AbsTextResource>()

        init {
            resources.forEach {
                resourcesMap[it.getTextName()] = it
            }
        }

        fun getTextResource(name: String): AbsTextResource? {
            return resourcesMap[name]
        }
    }
}
