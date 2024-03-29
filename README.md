# sri-efactura-core :ecuador:

Clases esenciales por la creación, la firma y la autorización de comprobantes electrónicos por los
servicios SRI en Ecuador. Empaquetado en una libraría Jar por Java 8+.

La mayoría del código se genera automáticamente por herramientas `xjc` or `wsimport`, con
los esquemas XSD y las especificaciones WSDL publicados por SRI.
Los esquemas XSD están disponibles aquí en [sri-efactura-model/.../META-INF/schema](sri-efactura-model/src/main/sri/META-INF/schema).
Los especificaciones WSDL están en [src-client/.../META-INF/wsdl](sri-client/src/main/resources/META-INF/wsdl).

Las clases generadas por los esquemas XSD son aumentadas con anotaciones de `jakarta.validation`, pues que las
instancias pueden ser validadas (por ejemplo con el [Hibernate validator](https://hibernate.org/validator/documentation/getting-started/))
antes de generar el XML.

Se incluye las clases de las librarías MITyCLib por firmas con XAdES BES -
los que no son disponibles por MavenCentral.

Aparte de la libraría `sri-efactura-core`, este repositorio se incluye también una utilidad de
línea de comandos por pruebas/depuración: ver [sri-soap-cli](sri-soap-cli)


## Construir la libraría sri-efactura-core :hammer_and_wrench:

Prerequisites:
- JDK >= Java SE 8

Steps:
1) Clone el repositorio y compílelo usando el script contenedor `gradlew`:
```console
$ git clone https://github.com/xprl-gjf/sri-efactura-core.git \
    && cd sri-efactura-core
$ ./gradlew clean build
```

2) Si lo desea, publique la biblioteca en un repositorio local de Maven:
```console
$ ./gradlew publishToMavenLocal
```
Este paso no es necesario si quiere usar la versión ya publicada en MavenCentral. 

## Para usar la libraría sri-efactura-core :jigsaw:

Inclúyalo en su proyecto por Gradle en la manera siguiente:

```kotlin
// build.gradle.kts:

repositories {
    // Elija una de los siguientes opciones:
    // Opción 1: [MavenCentral](https://repo1.maven.org/maven2/uk/co/xprl/efactura/sri-efactura-core/)
    mavenCentral()

    // Opción 2: Maven local cache
    mavenLocal {
        // if using mavenLocal, it is good practice to restrict it to only specific libs/groups
        content {
            includeGroup("uk.co.xprl.efactura")
        }
    }
}

dependencies {
    implementation("uk.co.xprl.efactura:sri-efactura-core:0.1.2")
    runtimeOnly("com.sun.xml.bind:jaxb-impl:4.0.0",) {
        because("Runtime implementation of jaxb-api")
    }

    // if performing validation using the jakarta.validation annotations:
    runtimeOnly("org.hibernate.validator:hibernate-validator:7.0.5.Final") {
        because("Runtime provider of jakarta.validation.Validator.")
    }
    runtimeOnly("org.glassfish:jakarta.el:4.0.2") {
        because("Runtime Expression Language implementation needed by jakarta.validation.Validator.")
    }

    // if signing comprobantes using XAdESBESSignature: 
    runtimeOnly("org.bouncycastle:bcprov-jdk18on:1.71.1") {
        because("Runtime support for BouncyCastleProvider; a security provider with support for PKCS12, for xades-firma")
    }
    runtimeOnly("commons-logging:commons-logging:1.2") {
        because("For es.mityc.javasign classes bundled from xades-firma")
    }

    // ...
}
```

Las clases esenciales están en packages:
* ec.gob.sri.\<comprobante\>.vX_X_X.\*  - generadas automáticamente por XSD, incluyendo la versión del esquema XSD, como `ec.gob.sri.factura.V1_1_0.Factura`,
* [uk.co.xprl.efactura.soap.client.*](sri-client/src/main/java/uk/co/xprl/efactura/soap/client) - proxies por servicios web de SRI
* [uk.co.xprl.efactura.XAdESBESSignature](xades-firma/src/main/java/uk/co/xprl/efactura/XAdESBESSignature.java) - para firmar documentos XML.


Por ejemplos, ver los [unit tests](sri-efactura-model/src/test/java/ec/gob/sri) y [sri-soap-cli](sri-soap-cli/src/main/kotlin)
