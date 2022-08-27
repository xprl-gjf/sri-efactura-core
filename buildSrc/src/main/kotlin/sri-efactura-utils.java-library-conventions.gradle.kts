@file:Suppress("PropertyName")

val java_language_version: String by project
val java_target_jdk_version: String by project

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.valueOf(java_language_version)
java.targetCompatibility = JavaVersion.valueOf(java_target_jdk_version)

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

