
import org.docopt.Docopt
import java.net.MalformedURLException
import java.net.URL
import kotlin.system.exitProcess

const val progName = "sri-soap-cli"
const val version = "0.1.0"
const val ENV_SRI_RECEPCION_WSDL = "SRI_RECEPCION_WSDL"
const val ENV_SRI_AUTORIZACION_WSDL = "SRI_AUTORIZACION_WSDL"
// default WSDL is for the SRI pruebas services
const val DEFAULT_SRI_RECEPCION_WSDL_URL = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl"
const val DEFAULT_SRI_AUTORIZACION_WSDL_URL = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"
const val errStatus = 1

const val usage = """
Usage:
  $progName [--wsdl=WSDL_URL] enviar <COMPROBANTE_FILE>
  $progName [--wsdl=WSDL_URL] autorizar <CLAVE_ACESSO>
  $progName --version
  $progName --help

Options:
  -w --wsdl=WSDL_URL                URL of SRI service WSDL.

If WSDL_URL is not specified, by default the WSDL for the SRI pruebas environment
will be selected:
 - ${DEFAULT_SRI_RECEPCION_WSDL_URL}
 - ${DEFAULT_SRI_AUTORIZACION_WSDL_URL}
Alternatively, WSDL urls may be configured via env vars:
${ENV_SRI_RECEPCION_WSDL} and ${ENV_SRI_AUTORIZACION_WSDL}.
"""

typealias Command = () -> Unit

fun main(args: Array<String>) {

    val parsedArgs = Docopt(usage)
        .withVersion("${progName} v${version}")
        .withHelp(true)
        .parse(args.toList())

    val command = when {
        parsedArgs["enviar"] as Boolean -> {
            val wsdlUrl = getWsdlUrl(parsedArgs, ENV_SRI_RECEPCION_WSDL, DEFAULT_SRI_RECEPCION_WSDL_URL)
            val comprobante: String = parsedArgs.getArgOrExit("<COMPROBANTE_FILE>")
            cmdEnviarComprobante(wsdlUrl, comprobante)
        }
        parsedArgs["autorizar"] as Boolean -> {
            val wsdlUrl = getWsdlUrl(parsedArgs, ENV_SRI_AUTORIZACION_WSDL, DEFAULT_SRI_AUTORIZACION_WSDL_URL)
            val claveAcesso: String = parsedArgs.getArgOrExit("<CLAVE_ACESSO>")
            cmdAutorizarComprobante(wsdlUrl, claveAcesso)
        }
        else -> null
    }

    command?.invoke()
}

private fun getWsdlUrl(args: Map<String, Any>, envVar: String, default: String): URL {
    // get URL value from either command-line argument or environment variable, or default fallback value.
    val urlStr = args["--wsdl"] as String?
        ?: System.getenv(envVar)
        ?: default

    return try {
        URL(urlStr)
    } catch (e: MalformedURLException) {
        System.err.println(e)
        errExit()
    }
}

private inline fun <T> Map<String, Any>.getArgOrExit(
    argName: String,
    exitHandler: () -> Nothing = { errExit(errStatus) },
    parse: (String) -> T = { defaultParse(it) }
): T {
    return try {
        val arg = (this[argName] as String)
        parse(arg) ?: exitHandler()
    } catch (e: Exception) {
        exitHandler()
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> defaultParse(it: String): T = it as T

private fun errExit(status: Int = errStatus): Nothing {
    System.err.println(usage)
    exitProcess(status)
}

