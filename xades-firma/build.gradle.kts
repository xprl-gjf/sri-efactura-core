
plugins {
    id("sri-efactura-utils.java-library-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation("com.googlecode.xades4j:xades4j:2.1.0")
    implementation("org.jetbrains:annotations:23.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.xmlunit:xmlunit-assertj:2.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.5")
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveBaseName.set("xades-firma")
    archiveClassifier.set("")
}

artifacts {
    archives(tasks.shadowJar)
}
