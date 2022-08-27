import com.fasterxml.jackson.databind.ObjectMapper
import ec.com.xprl.efactura.soap.client.AutorizacionComprobanteProxy
import java.net.URL

fun cmdAutorizarComprobante(
    wsdlLocation: URL,
    claveAcesso: String
): Command = {
    val proxy = AutorizacionComprobanteProxy(wsdlLocation)
    val respuesta = proxy.autorizacionIndividual(claveAcesso)

    val objectMapper = ObjectMapper()
    objectMapper.writeValue(System.out, respuesta)
    println()
}
