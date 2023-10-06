package uk.co.xprl.efactura.soap.client;

import ec.gob.sri.ws.recepcion.RecepcionComprobantesOffline;
import ec.gob.sri.ws.recepcion.RecepcionComprobantesOfflineService;
import ec.gob.sri.ws.recepcion.RespuestaSolicitud;
import jakarta.xml.ws.BindingProvider;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Proxy class for the RecepciónComprobantesOffline web service.
 */
public class EnvioComprobantesProxy {

    private final RecepcionComprobantesOffline port;

    /**
     * Create a new {@link EnvioComprobantesProxy}.
     *
     * @param wsdlLocation  the URL of the Web Service Description Language resource that
     *                      defines the web service endpoint represented by this proxy instance.
     *
     * @see WsdlLocations#SRI_PRUEBAS_RECEPCION_COMPROBANTES_WSDL
     * @see WsdlLocations#SRI_PRODUCCION_RECEPCION_COMPROBANTES_WSDL
     */
    public EnvioComprobantesProxy(URL wsdlLocation) {
        this(wsdlLocation, null);
    }

    /**
     * Create a new {@link EnvioComprobantesProxy}.
     *
     * @param wsdlLocation  the URL of the Web Service Description Language resource that
     *                      defines the web service endpoint represented by this proxy instance.
     *
     * @param requestContextConfig  Action to configure the request context properties.
     *
     * @see WsdlLocations#SRI_PRUEBAS_RECEPCION_COMPROBANTES_WSDL
     * @see WsdlLocations#SRI_PRODUCCION_RECEPCION_COMPROBANTES_WSDL
     */
    public EnvioComprobantesProxy(URL wsdlLocation, Consumer<Map<String, Object>> requestContextConfig) {
        QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
        RecepcionComprobantesOfflineService service = new RecepcionComprobantesOfflineService(wsdlLocation, qname);

        port = service.getRecepcionComprobantesOfflinePort();
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        if (requestContextConfig != null) {
            requestContextConfig.accept(requestContext);
        }
    }

    /**
     * Enviar un comprobante electrónico al servicio RecepciónComprobantesOffline.
     *
     * @param archivoBytes  comprobante electrónico en forma XML, serializado en bytes por encoding UTF8.
     * @return              respuesta del servicio web como {@link RespuestaSolicitud}
     */
    public RespuestaSolicitud enviarComprobante(byte[] archivoBytes) {
        return port.validarComprobante(archivoBytes);
    }
}