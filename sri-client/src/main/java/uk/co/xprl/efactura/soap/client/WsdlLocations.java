package uk.co.xprl.efactura.soap.client;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLs bien conocidos por los servicios web de SRI
 */
public class WsdlLocations {

    /**
     * URL <a href="https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl">https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl</a>
     */
    public static final URL SRI_PRUEBAS_RECEPCION_COMPROBANTES_WSDL = createURL(
            "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl"
    );

    /**
     * URL <a href="https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl">https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl</a>
     */
    public static final URL SRI_PRUEBAS_AUTORIZACION_COMPROBANTES_WSDL = createURL(
            "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"
    );

    /**
     * URL <a href="https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl">https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl</a>
     */
    public static final URL SRI_PRODUCCION_RECEPCION_COMPROBANTES_WSDL = createURL(
            "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl"
    );

    /**
     * URL <a href="https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl">https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl</a>
     */
    public static final URL SRI_PRODUCCION_AUTORIZACION_COMPROBANTES_WSDL = createURL(
            "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"
    );


    private static URL createURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
