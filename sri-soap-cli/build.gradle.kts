
plugins {
    id("sri-efactura-core.kotlin-application-conventions")
}

dependencies {
    implementation(project(":sri-client"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
    implementation("com.offbytwo:docopt:0.6.0.20150202")
}
repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}
