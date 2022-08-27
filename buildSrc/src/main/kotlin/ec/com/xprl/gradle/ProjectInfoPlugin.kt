package ec.com.xprl.gradle

import com.typesafe.config.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.*
import kotlinx.serialization.SerializationException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.extra
import java.io.File

/**
 * Gradle plugin to parse a HOCON properties file containing
 * extended project info, and populate project properties accordingly.
 */
class ProjectInfoPlugin: Plugin<Project> {

    companion object {
        // a cache of parsed project info config files
        private val cache = emptyMap<File, Config>().toMutableMap()

        private fun getConfig(file: File, logger: Logger? = null): Config =
            cache.computeIfAbsent(file) {
                logger?.quiet("Reading project info from file ${file.path}...")
                ConfigFactory.parseFile(file)      // May throw ConfigException...
            }
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create<ProjectInfoPluginExtension>("projectInfo", project)
        val file: File = extension.projectInfoFile

        val projectInfo = tryParseProjectInfo(project, file)
        projectInfo?.apply {
            with(project) {
                extra["projectInfo"] = projectInfo
                version = projectInfo.version
                group = projectInfo.group
            }
        }
    }

    /**
     * Generate ProjectInfo by parsing the given file.
     */
    private fun tryParseProjectInfo(project: Project, file: File): ProjectInfo? {
        try {
            val config = getConfig(file, project.logger)      // May throw ConfigException...
            return parseProjectConfig(project, config)        // May throw SerializationException...
        } catch (e: ConfigException) {
            // failed to parse
            project.logger.warn("Failed to read project info from ${file.path}: \n{}", e.toString())
        } catch (e: SerializationException) {
            // failed to parse
            project.logger.warn("Failed to read project info from ${file.path}: \n{}", e.toString())
        }
        return null
    }
}

/**
 * [ProjectInfoPlugin] extension to configure the project info file path.
 *
 * By default, the project info file is "projectInfo.conf" under the root project.
 */
open class ProjectInfoPluginExtension(val project: Project) {
    var projectInfoFile by gradleProperty<ProjectInfoPluginExtension, File>(project, default=project.rootProject.file("projectInfo.conf"))
}

/**
 * Parse the project info file as a HOCON config file.
 *
 * The parser configuration supports value substitutions to be resolved
 * against a limited set of existing project properties.
 *
 * Typical usage for this is:
 *   version = ${version}       // resolves to current project version value
 *   group = ${group}           // resolves to current project group value
 */
@ExperimentalSerializationApi
private fun parseProjectConfig(project: Project, config: Config): ProjectInfo {
    val allowedSubstitutions = setOf("version", "group")
    val fallbackProperties = project.properties
        .filterKeys { it in allowedSubstitutions }
        .filterValues { it is String }
    val fallbackConfig = ConfigFactory.parseMap(fallbackProperties)

    val resolved = config.resolveWith(fallbackConfig,
        ConfigResolveOptions.noSystem().setAllowUnresolved(true))

    return Hocon.Default.decodeFromConfig(resolved)       // May throw SerializationException
}

