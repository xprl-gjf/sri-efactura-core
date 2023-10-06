import com.github.jengelman.gradle.plugins.shadow.transformers.*

plugins {
    id("sri-efactura-utils.java-library-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("uk.co.xprl.maven-artifact")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":sri-client"))
    compileOnly(project(":sri-efactura-model"))
    compileOnly(project(":xades-firma", configuration="shadow"))
}

/*
tasks.build {
    dependsOn(tasks.shadowJar)
}
*/

tasks.jar {
    enabled = true
    dependsOn(tasks.shadowJar)
}

java {
    withJavadocJar()
    withSourcesJar()
}

// TODO: find a way to include embedded resources (e.g. XSD and WSDL files) from the bundled subproject jar files
tasks.shadowJar {
    archiveBaseName.set("sri-efactura-core")
    archiveClassifier.set("")
    // bundle only the compile-time dependencies in this jar
    configurations = listOf(project.configurations.compileClasspath.get())

    /*
    transform(IncludeResourceTransformer::class.java) {
        resource = "META-INF/schema/factura_V1.0.0.xsd"
        file = file("META-INF/schema/factura_V1.0.0.xsd")
    }
     */
}

mavenArtifact {
    artifactId = "sri-efactura-core"
    artifactName = "SRI efactura core"
    artifactDescription = "Core classes for creating, signing and authorising comprobantes electrónicos via web services of SRI in Ecuador"
}

/*
 * Remove the "apiElements" and "runtimeElements" components;
 * these would be empty and are not useful.
 */
val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["apiElements"]) {
    skip()
}
javaComponent.withVariantsFromConfiguration(configurations["runtimeElements"]) {
    skip()
}

/*
 * Publish the generated shadow Jar to a Maven repository.
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
