# sri-soap-cli :ecuador:

Cliente por los servicios de web SRI (Servicio de Rentas Internas) de Ecuador.

Se desarrolla en Kotlin por JRE 11+. Se arma por Gradle.

## Usando el sri-soap-cli :computer:

Prerequisites por `sri-soap-cli`:
- JDK >= Java SE 11

Construir e instalarlo:
```console
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

