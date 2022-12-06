
plugins {
    id("sri-efactura-utils.java-library-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    api(files(
        "additional_libs/MITyCLibAPI-1.0.4.jar",
        "additional_libs/MITyCLibXADES-1.0.4.jar",
    ))
    implementation(files(
        "additional_libs/MITyCLibCert-1.0.4.jar",
        "additional_libs/jss-4.2.5.jar",
    ))
    runtimeOnly(files(
        "additional_libs/MITyCLibCert-1.0.4.jar",
        "additional_libs/MITyCLibOCSP-1.0.4.jar",
        "additional_libs/MITyCLibPolicy-1.0.4.jar",
        "additional_libs/MITyCLibTrust-1.0.4.jar",
        "additional_libs/MITyCLibTSA-1.0.4.jar",
        "additional_libs/xmlsec-1.4.2-ADSI-1.0.jar",
    ))

    testCompileOnly("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.xmlunit:xmlunit-assertj:2.9.0")
    testImplementation("org.bouncycastle:bcprov-jdk18on:1.72") {
        because("Runtime support for BouncyCastleProvider; a security provider with support for PKCS12, for xades-firma")
    }
    testRuntimeOnly("commons-logging:commons-logging:1.2") {
        because("For es.mityc.javasign classes bundled from xades-firma")
    }
}
repositories {
    mavenCentral()
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
