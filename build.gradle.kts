plugins {
    // read extended project info from "projectInfo.conf"
    id("ec.com.xprl.project-info")
    id("sri-efactura-core.java-library-conventions")
    id("ec.com.xprl.maven-artifact")
}

evaluationDependsOnChildren()

val version: String by project
val jarProjects = arrayOf(
    ":sri-client",
    ":sri-efactura-model",
    ":xades-firma"
)

val projectVersion = version
tasks.jar {
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
    archiveBaseName.set("sri-efactura-core")
    archiveVersion.set(projectVersion)
    from(
        jarProjects.map { projectName ->
            project(projectName).sourceSets.main.get().output
        }
    )
}

// TODO: tidy up some of the resource content under META-INF/wsdl

// dependencies to be listed in the generated jar
dependencies {
    api("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("com.sun.xml.ws:jaxws-rt:4.0.0")
    implementation("com.googlecode.xades4j:xades4j:2.1.0")
}

mavenArtifact {
    artifactId = "sri-efactura-core"
    artifactName = "SRI efactura core"
    artifactDescription =
        "Core classes for creating, signing and authorising comprobantes electrónicos via web services of SRI in Ecuador"
}

/*
 * Publish the generated uber jar to a Maven repository.
 */
publishing {
    repositories {
/*
maven {
    // change URLs to point to your repos, e.g. http://my.org/repo
    val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
    val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
}
 */
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/xprl-gjf/sri-efactura-core")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
