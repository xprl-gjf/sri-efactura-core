/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.xprl.efactura;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.ObjectToSign;
import org.w3c.dom.Document;

/**
 *
 * @author Usuario
 */
public class XAdESBESSignature extends GenericXMLSignature {

    public Document firmarDocumento(byte[] archivo, byte[] firmaCliente, 
            String clave, String descripcion) throws Exception {
        return this.execute(archivo, firmaCliente, clave, descripcion);
    }

    protected DataToSign createDataToSign(byte[] archivo, String descripcion) throws Exception {
        DataToSign dataToSign = new DataToSign();
        dataToSign.setXadesFormat(EnumFormatoFirma.XAdES_BES);
        dataToSign.setEsquema(XAdESSchemas.XAdES_132);
        dataToSign.setXMLEncoding("UTF-8");
        dataToSign.setEnveloped(true);
        //dataToSign.setBaseURI("#comprobante");
        dataToSign.addObject(new ObjectToSign(
                new XMLComprobante(), descripcion, null, "text/xml", null));
        Document docToSign = getDocument(archivo);
        dataToSign.setDocument(docToSign);
        return dataToSign;
    }

    protected String getSignatureFileName() {
        return "archivo";
    }
    
}
