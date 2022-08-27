import com.fasterxml.jackson.databind.ObjectMapper
import ec.com.xprl.efactura.soap.client.EnvioComprobantesProxy
import java.io.File
import java.net.URL

fun cmdEnviarComprobante(
    wsdlLocation: URL,
    comprobanteFilePath: String
): Command = {
    val proxy = EnvioComprobantesProxy(wsdlLocation)
    val file = File(comprobanteFilePath)
    val respuesta = proxy.enviarComprobante(file.readBytes())

    val objectMapper = ObjectMapper()
    objectMapper.writeValue(System.out, respuesta)
    println()
}
