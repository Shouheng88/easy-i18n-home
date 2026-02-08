package me.shouheng.i18n.manager

import androidx.compose.runtime.Composable
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.edit_rule_array_detail
import easy_i18n.composeapp.generated.resources.edit_rule_plural_detail
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.data.model.I18nPlatform
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.data.model.PathDetected
import me.shouheng.i18n.utils.settings
import org.jetbrains.compose.resources.stringResource
import kotlin.collections.forEach

/** 多语言相关的管理类 */
object I18nManager {

    private const val KEY_WORKING_PROJECT_ID = "__i18n_working_project_id__"
    private const val KEY_WORKING_PLATFORM_ID = "__i18n_working_platform_id_of_project__"
    private const val KEY_WORKING_PATH_OF_PROJECT_PLATFORM = "__i18n_working_path_of_project_%d_platform_%d__"

    fun getWorkingProjectId(): Long? = settings.getLongOrNull(KEY_WORKING_PROJECT_ID)

    fun setWorkingProject(project: I18nProject) {
        settings.putLong(KEY_WORKING_PROJECT_ID, project.id)
    }

    fun getWorkingPlatformOf(project: I18nProject?): I18nPlatform {
        project ?: return I18nPlatform.Android
        val key = KEY_WORKING_PLATFORM_ID.format(project.id)
        return I18nPlatform.from(settings.getInt(key, I18nPlatform.Android.id))
    }

    fun setWorkingPlatform(project: I18nProject?, platform: I18nPlatform) {
        project ?: return
        val key = KEY_WORKING_PLATFORM_ID.format(project.id)
        settings.putInt(key, platform.id)
    }

    fun getWorkingPath(project: I18nProject?, platform: I18nPlatform?): String? {
        project ?: return null
        platform ?: return null
        val key = KEY_WORKING_PATH_OF_PROJECT_PLATFORM.format(project.id, platform.id)
        return settings.getStringOrNull(key)
    }

    fun setWorkingPath(project: I18nProject?, platform: I18nPlatform?, path: I18nPath) {
        project ?: return
        platform ?: return
        val key = KEY_WORKING_PATH_OF_PROJECT_PLATFORM.format(project.id, platform.id)
        settings.putString(key, path.path)
    }

    /** 扫描指定的目录 */
    fun detect(root: PlatformFile, platform: I18nPlatform): List<PathDetected> {
        val matched = mutableListOf<PathDetected>()

        if (!root.isDirectory()) {
            return matched
        }

        val directories = mutableListOf<PlatformFile>()
        directories.add(root)
        while (directories.isNotEmpty()) {
            val directory = directories.removeAt(0)
            if (directory.isDirectory()) {
                val types = getResourceTypes(directory, platform)
                if (types.isNotEmpty()) {
                    types.forEach {
                        matched.add(PathDetected(directory, root, it))
                    }
                    // 继续遍历，它不一定只用作多语言目录...可能子目录里面还有多语言
                    directories.addAll(directory.list())
                    continue
                }
                // 判断是否需要忽略该目录：比如是 build 目录等
                if (!shouldIgnoreDirectory(directory, platform)) {
                    val children = directory.list()
                    directories.addAll(children)
                }
            }
        }

        return matched
    }

    /** 复数类型的编辑规则详细说明 */
    @Composable
    fun getPluralEditRule(): String = stringResource(Res.string.edit_rule_plural_detail).trimIndent()

    /** 数组类型的编辑规则详细说明 */
    @Composable
    fun getArrayEditRule(): String = stringResource(Res.string.edit_rule_array_detail).trimIndent()

    /** 是否应该忽略指定的目录 */
    private fun shouldIgnoreDirectory(directory: PlatformFile, platform: I18nPlatform): Boolean {
        return when(platform) {
            I18nPlatform.Android, I18nPlatform.ComposeMultiplatform -> {
                val isBuildDirectory = directory.name.equals("build", ignoreCase = true)
                        && directory.list().any { it.name.equals("generated", ignoreCase = true) }
                return isBuildDirectory
            }
            I18nPlatform.JavaProperties -> {
                val isBuildDirectory = directory.name.equals("build", ignoreCase = true)
                        && directory.list().any { it.name.equals("generated", ignoreCase = true) }
                val isTargetDirectory = directory.name.equals("target", ignoreCase = true)
                        && directory.list().any { it.name.equals("classes", ignoreCase = true) }
                return isBuildDirectory || isTargetDirectory
            }
            I18nPlatform.IOS -> false
            I18nPlatform.Flutter -> {
                val isBuildDirectory = directory.name.equals("build", ignoreCase = true)
                        && directory.list().any {
                    it.name.equals("generated", ignoreCase = true) ||
                            it.name.equals("app", ignoreCase = true)
                }
                return isBuildDirectory
            }
        }
    }

    /** 判断指定的目录是否为目标多语言目录 */
    private fun getResourceTypes(directory: PlatformFile, platform: I18nPlatform): List<I18nResourceType> {
        val types = mutableListOf<I18nResourceType>()
        when (platform) {
            I18nPlatform.Android -> {
                if (directory.name.equals("res", ignoreCase = true)
                    && directory.list().any { it.name.startsWith("values", ignoreCase = true) }) {
                    types.add(I18nResourceType.AndroidXML)
                }
            }
            I18nPlatform.IOS -> {
                if (directory.list().any { it.name.endsWith(".lproj") }) {
                    types.add(I18nResourceType.IOSCStrings)
                }
                if (directory.list().any { it.name.endsWith(".xcstrings") }) {
                    types.add(I18nResourceType.IOSXCStrings)
                }
            }
            I18nPlatform.ComposeMultiplatform -> {
                val isResourcesDirectory = directory.name.equals("composeResources", ignoreCase = true)
                        && directory.list().any { it.name.startsWith("values", ignoreCase = true) }
                if (isResourcesDirectory) {
                    types.add(I18nResourceType.ComposeMultiplatformXML)
                }
            }
            I18nPlatform.JavaProperties -> {
                if (directory.list().any { it.name.endsWith(".properties", ignoreCase = true) }) {
                    types.add(I18nResourceType.JavaProperties)
                }
            }
            I18nPlatform.Flutter -> {
                if (directory.list().any { it.name.endsWith(".arb", ignoreCase = true) }) {
                    types.add(I18nResourceType.FlutterArb)
                }
            }
        }
        return types
    }
}