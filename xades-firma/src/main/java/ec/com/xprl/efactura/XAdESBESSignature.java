package ec.com.xprl.efactura;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.*;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.providers.KeyingDataProvider;
import java.io.File;

/**
 * Firmador para agregar una firma en forma Xades BES para
 * comprobantes electrónicos de XML.
 */
public class XAdESBESSignature extends GenericXMLSignature {

    /**
     * Firmar un comprobante electrónico.
     * @param archivo el contenido XML del comprobante electrónico.
     * @param firmaCliente el KeyStore PKS12 que contiene el certificado por la firma.
     * @param clave contraseña para abrir el KeyStore.
     * @param descripcion una descripción de los datos que va a estar firmados (opcional).
     * @return el documento firmado.
     * @throws Exception el proceso de firmar el documento falló.
     */
    public Document firmarDocumento(byte[] archivo, File firmaCliente,
            String clave, String descripcion) throws Exception {
        return this.execute(archivo, firmaCliente, clave, descripcion);
    }

    @Override
    protected @NotNull XadesSigningProfile getXadesSigningProfile(KeyingDataProvider kp) {
        return new XadesBesSigningProfile(kp);
    }

    @Override
    protected @NotNull SignedDataObjects createDataToSign(@NotNull Document document, String descripcion) {
        DataObjectDesc obj = new DataObjectReference("#comprobante")
            .withDataObjectFormat(createXmlDataObjectFormatProperty(descripcion))
            .withTransform(new EnvelopedSignatureTransform());
        return new SignedDataObjects(obj);
    }

    static @NotNull DataObjectFormatProperty createXmlDataObjectFormatProperty(String descripcion)
    {
        DataObjectFormatProperty result = new DataObjectFormatProperty("text/xml");
        if (descripcion != null)
        {
            return result.withDescription(descripcion);
        }
        return result;
    }

    @Override
    protected Element getSignatureParentNode(@NotNull Document document)
    {
        return identifyIdNodes(document)
            .getElementById("comprobante");
    }

    private static @NotNull Document identifyIdNodes(@NotNull Document document)
    {
        document.getDocumentElement().setIdAttributeNS(null, "id", true);
        return document;
    }
}
