# sri-soap-cli :ecuador:

Cliente por los servicios de web SRI (Servicio de Rentas Internas) de Ecuador.

Incluye también una utilidad de línea de comandos por pruebas/depuración.

El código Java por los servicios de web fue generado automáticamente del WSDL, por medio de JAX-WS wsimport.
Pero ver el [wsdl README](lib/src/main/resources/META-INF/wsdl/README.md) por mas información.

Armado por Gradle. La libraría puede estar empaquetado y distribuido por Maven.

## Construir la libraría sri-soap-client :wrench:

Prerequisites:
- JDK >= Java SE 8

Steps:
1) Clone el repositorio y compílelo usando el script contenedor `gradlew`:
```shell
$ git clone https://github.com/xprl-gjf/sri-soap-client.git \
    && cd sri-soap-client
$ ./gradlew clean build
```

2) Si lo desea, publique la biblioteca en un repositorio local de Maven:
```shell
$ ./gradlew publishToMavenLocal
```

## Usando el sri-soap-cli :computer:

Prerequisites por `sri-soap-cli`:
- JDK >= Java SE 11

Construir e instalarlo:
```shell
$ ./gradlew :sri-soap-cli:installDist && cd ./build/install/sri-soap-cli/bin
$ ./sri-soap-cli --version 
```

Usage:
```text
Usage:
  sri-soap-cli [--wsdl=WSDL_URL] enviar <COMPROBANTE_FILE>
  sri-soap-cli [--wsdl=WSDL_URL] autorizar <CLAVE_ACESSO>
  sri-soap-cli --version
  sri-soap-cli --help

Options:
  -w --wsdl=WSDL_URL                URL of SRI service WSDL.

If WSDL_URL is not specified, by default the WSDL for the SRI pruebas environment
will be selected:
 - https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl
 - https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl
Alternatively, WSDL urls may be configured via env vars:
SRI_RECEPCION_WSDL and SRI_AUTORIZACION_WSDL.
```

#### Más Opciones de Configuración :gear:

Para ver detalles de los mensajes enviados y recibidos, aumente la opción de Java:
`-Dcom.sun.xml.ws.transport.http.client.HttpTransportPipe.dump=true`.

Para usar un trustStore con el certificado SSL por los servidores SRI, guardar los
certificados en un archivo en forma PKCS#12 y aumente las opciones:
`-Djavax.net.ssl.trustStore=/path/to/trustStore.pfx -Djavax.net.ssl.trustStorePassword=password`.

