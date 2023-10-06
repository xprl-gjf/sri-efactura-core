package uk.co.xprl.efactura.soap.client;

import ec.gob.sri.ws.autorizacion.AutorizacionComprobantesOffline;
import ec.gob.sri.ws.autorizacion.AutorizacionComprobantesOfflineService;
import ec.gob.sri.ws.autorizacion.RespuestaComprobante;

import javax.xml.namespace.QName;

import ec.gob.sri.ws.autorizacion.RespuestaLote;
import jakarta.xml.ws.BindingProvider;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Proxy class for the AutorizaciónComprobantesOffline web service.
 */
public class AutorizacionComprobanteProxy {

    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_REQUEST_TIMEOUT = 10000;

    private final AutorizacionComprobantesOffline port;

    /**
     * Create a new {@link AutorizacionComprobanteProxy}.
     *
     * @param wsdlLocation  URL of the Web Service Description Language resource that
     *                      defines the web service endpoint represented by this proxy instance.
     *
     * @see WsdlLocations#SRI_PRUEBAS_AUTORIZACION_COMPROBANTES_WSDL
     * @see WsdlLocations#SRI_PRODUCCION_AUTORIZACION_COMPROBANTES_WSDL
     */
    public AutorizacionComprobanteProxy(URL wsdlLocation) {
        this(wsdlLocation, null);
    }

    /**
     * Create a new {@link AutorizacionComprobanteProxy}.
     *
     * @param wsdlLocation  URL of the Web Service Description Language resource that
     *                      defines the web service endpoint represented by this proxy instance.
     *
     * @param requestContextConfig  Action to configure the request context properties.
     *
     * @see WsdlLocations#SRI_PRUEBAS_AUTORIZACION_COMPROBANTES_WSDL
     * @see WsdlLocations#SRI_PRODUCCION_AUTORIZACION_COMPROBANTES_WSDL
     */
    public AutorizacionComprobanteProxy(URL wsdlLocation, Consumer<Map<String, Object>> requestContextConfig) {
        QName qname = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
        AutorizacionComprobantesOfflineService service = new AutorizacionComprobantesOfflineService(
                wsdlLocation, qname);

        port = service.getAutorizacionComprobantesOfflinePort();
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", DEFAULT_CONNECT_TIMEOUT);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", DEFAULT_REQUEST_TIMEOUT);

        if (requestContextConfig != null) {
            requestContextConfig.accept(requestContext);
        }
    }

    /**
     *
     * @param accessKey  clave para identificar de forma exclusiva el comprobante electrónico.
     * @return           respuesta del servicio web como {@link RespuestaComprobante}.
     */
    public RespuestaComprobante autorizacionIndividual(String accessKey) {
        return port.autorizacionComprobante(accessKey);
    }

    /**
     *
     * @param accessKey  clave para identificar de forma exclusiva el lote de comprobantes electrónicos.
     * @return           respuesta del servicio web como {@link RespuestaLote}.
     */
    public RespuestaLote autorizacionLote(String accessKey) {
        return port.autorizacionComprobanteLote(accessKey);
    }
}

