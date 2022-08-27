package ec.com.xprl.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

/**
 * Gradle plugin to simplify publishing a maven artifact to configured repositories
 * (using the Gradle built-in 'maven-publish' plugin),
 * by supplying project information via the ec.com.xprl.gradle.ProjectInfoPlugin.
 */
class MavenArtifactPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<MavenArtifactPluginExtension>("mavenArtifact", project)

        // apply the MavenPublishPlugin to allow the artifact to be added to the list of publications
        project.pluginManager.apply(org.gradle.api.publish.maven.plugins.MavenPublishPlugin::class.java)
        val publishingExtension = project.extensions["publishing"] as PublishingExtension

        project.afterEvaluate {
            // search up through the project hierarchy for projectInfo,
            // (but ignore the ProjectInfoPluginExtension named 'projectInfo'!)
            val projectInfoProperty = project.findProperty("projectInfo")
            val projectInfo = if (projectInfoProperty is ProjectInfo) { projectInfoProperty } else null

            // apply maven publication configuration after project has been configured
            this.addMavenPublication(projectInfo, extension, publishingExtension)
        }
    }

    /**
     * Add a MavenPublication instance for this project.
     */
    private fun Project.addMavenPublication(
        projectInfo: ProjectInfo?,
        artifactExtension: MavenArtifactPluginExtension,
        publishingExtension: PublishingExtension
    ) {
        val publicationName = artifactExtension.artifactId.toCamelCase()

        val licenseInfo = projectInfo?.license
        val projectUrl = projectInfo?.projectUrl
        val developers = (projectInfo?.developers as Array<*>?)
            ?.filterIsInstance<DeveloperInfo>()
            ?: emptyList()

        publishingExtension.publications.create<MavenPublication>(publicationName) {
            artifactId = artifactExtension.artifactId
            from(project.components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(artifactExtension.artifactName)
                description.set(artifactExtension.artifactDescription)
                projectUrl?.let {
                    url.set(it)
                }
                licenseInfo?.let {
                    licenses {
                        license {
                            name.set(it.name)
                            url.set(it.url)
                        }
                    }
                }
                developers {
                    for(dev in developers) {
                        developer {
                            id.set(dev.id)
                            name.set(dev.name)
                            dev.email?.let { email.set(it) }
                            dev.organization?.let { organization.set(it) }
                        }
                    }
                }
                /*
            scm {
                connection.set("scm:git:git://example.com/my-library.git")
                developerConnection.set("scm:git:ssh://example.com/my-library.git")
                url.set("http://example.com/my-library/")
            }
             */
            }
        }
    }
}

open class MavenArtifactPluginExtension(project: Project) {
    var artifactId by gradleProperty(project, project.name)
    var artifactName by gradleProperty(project, "")
    var artifactDescription by gradleProperty(project, "")
}

private fun String.toCamelCase(): String {
    val parts = this.toLowerCase().split('-', '_', '.', ':')
    val capitalized = parts.joinToString("") { it.capitalize() }
    return capitalized.decapitalize()   // set first character to lowercase
}
