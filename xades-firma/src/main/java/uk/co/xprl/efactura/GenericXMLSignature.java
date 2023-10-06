package uk.co.xprl.efactura;

import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.javasign.pkstore.CertStoreException;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class GenericXMLSignature {

    /**
     * <p>
     * Ejecución del ejemplo. La ejecución consistirá en la firma de los datos
     * creados por el método abstracto <code>createDataToSign</code> mediante el
     * certificado declarado en la constante <code>PKCS12_FILE</code>. El
     * resultado del proceso de firma será almacenado en un fichero XML en el
    
     * directorio correspondiente a la constante <code>OUTPUT_DIRECTORY</code>
     * del usuario bajo el nombre devuelto por el método abstracto
     * <code>getSignFileName</code>
     * </p>
     */
    protected Document execute(byte[] archivo, byte[] firmaCliente, String clave, String descripcion) throws Exception {

        // Obtencion del gestor de claves
        IPKStoreManager storeManager = getPKStoreManager(firmaCliente, clave);
        if (storeManager == null) {
            throw new Exception("El gestor de claves no se ha obtenido correctamente.");
        }

        // Obtencion del certificado para firmar. Utilizaremos el primer
        // certificado del almacen.
        X509Certificate certificate = getFirstCertificate(storeManager);
        if (certificate == null) {
            throw new Exception("No existe ningún certificado para firmar.");
        }

        // Obtención de la clave privada asociada al certificado
        PrivateKey privateKey;
        try {
            privateKey = storeManager.getPrivateKey(certificate);
        } catch (CertStoreException e) {
            throw new Exception("Error al acceder al almacén. "+e.toString());
        }

        // Obtención del provider encargado de las labores criptográficas
        Provider provider = storeManager.getProvider(certificate);

        /*
         * Creación del objeto que contiene tanto los datos a firmar como la
         * configuración del tipo de firma
         */
        DataToSign dataToSign = createDataToSign(archivo, descripcion);

        // Firmamos el documento
        Document docSigned = null;
        try {
            /*
             * Creación del objeto encargado de realizar la firma
             */
            FirmaXML firma = createFirmaXML();
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error realizando la firma. "+ex.toString());
        }
        return docSigned;
    }

    /**
     * <p>
     * Crea el objeto DataToSign que contiene toda la información de la firma
     * que se desea realizar. Todas las implementaciones deberán proporcionar
     * una implementación de este método
     * </p>
     * 
     * @return El objeto DataToSign que contiene toda la información de la firma
     *         a realizar
     */
    protected abstract DataToSign createDataToSign(byte[] archivo, String descripcion) throws Exception;

    /**
     * <p>
     * Nombre del fichero donde se desea guardar la firma generada. Todas las
     * implementaciones deberán proporcionar este nombre.
     * </p>
     * 
     * @return El nombre donde se desea guardar la firma generada
     */
    protected abstract String getSignatureFileName();

    /**
     * <p>
     * Crea el objeto <code>FirmaXML</code> con las configuraciones necesarias
     * que se encargará de realizar la firma del documento.
     * </p>
     * <p>
     * En el caso más simple no es necesaria ninguna configuración específica.
     * En otros casos podría ser necesario por lo que las implementaciones
     * concretas de las diferentes firmas deberían sobreescribir este método
     * (por ejemplo para añadir una autoridad de sello de tiempo en aquellas
     * firmas en las que sea necesario)
     * <p>
     * 
     * @return firmaXML Objeto <code>FirmaXML</code> configurado listo para usarse
     */
    protected FirmaXML createFirmaXML() {
        return new FirmaXML();
    }

    /**
     * <p>
     * Escribe el documento a un fichero.
     * </p>
     * 
     * @param document El documento a imprmir
     * @param pathfile El path del fichero donde se quiere escribir.
     */
    public void saveDocumentToFile(Document document, String pathfile) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(pathfile);
            UtilidadTratarNodo.saveDocumentToOutputStream(document, fos, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error al guardar el documento firmado. "+e.toString());
        }
    }

    /**
     * <p>
     * Escribe el documento a un fichero. Esta implementacion es insegura ya que
     * dependiendo del gestor de transformadas el contenido podría ser alterado,
     * con lo que el XML escrito no sería correcto desde el punto de vista de
     * validez de la firma.
     * </p>
     * 
     * @param document El documento a imprmir
     * @param pathfile El path del fichero donde se quiere escribir.
     */
    private void saveDocumentToFileUnsafeMode(Document document, String pathfile) throws Exception {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();

            serializer.transform(new DOMSource(document), new StreamResult(new File(pathfile)));
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new Exception("Error al guardar el documento firmado. "+e.toString());
        }
    }

    /**
     * <p>
     * Devuelve el <code>Document</code> correspondiente al
     * <code>resource</code> pasado como parámetro
     * </p>
     * 
     * @param resource El recurso que se desea obtener
     * @return El <code>Document</code> asociado al <code>resource</code>
     */
    protected Document getDocument(byte[] resource) throws Exception {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
        	InputStream b = new ByteArrayInputStream(resource);
            doc = dbf.newDocumentBuilder().parse(b);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            throw new Exception("Error al parsear el documento. "+ex.toString());
        } catch (SAXException ex) {
            ex.printStackTrace();
            throw new Exception("Error al parsear el documento. "+ex.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("Error al parsear el documento."+ex.toString());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw new Exception("Error al parsear el documento."+ex.toString());
        }
        return doc;
    }

    /**
     * <p>
     * Devuelve el contenido del documento XML
     * correspondiente al <code>resource</code> pasado como parámetro
     * </p> como un <code>String</code>
     * 
     * @param resource El recurso que se desea obtener
     * @return El contenido del documento XML como un <code>String</code>
     */
    protected String getDocumentAsString(byte[] resource) throws Exception {
        Document doc = getDocument(resource);
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        StringWriter stringWriter = new StringWriter();
        try {
            serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stringWriter));
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new Exception("Error al obtener el contenido del documento. "+e.toString());
        }

        return stringWriter.toString();
    }

    /**
     * <p>
     * Devuelve el gestor de claves que se va a utilizar
     * </p>
     * 
     * @return El gestor de claves que se va a utilizar</p>
     */
    private IPKStoreManager getPKStoreManager(byte[] firma, String clave) throws Exception {
        IPKStoreManager storeManager = null;
        try {
            InputStream b = new ByteArrayInputStream(firma);
            
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(b, clave.toCharArray());
            storeManager = new KSStore(ks, new PassStoreKS(clave));
            
//                KeyStore ks = KeyStore.getInstance("JKS");
//                ks.load(b.getInputStream(), clave.toCharArray());
//                Provider provider = Security.getProvider("SunRsaSign");
//                storeManager = new KSStore(ks, provider, new PassStoreKS(clave));
            
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
            throw new Exception("No se puede generar KeyStore PKCS12. "+ex.toString());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new Exception("No se puede generar KeyStore PKCS12. "+ex.toString());
        } catch (CertificateException ex) {
            ex.printStackTrace();
            throw new Exception("No se puede generar KeyStore PKCS12. "+ex.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("No se puede generar KeyStore PKCS12. "+ex.toString());
        }
        return storeManager;
    }

    /**
     * <p>
     * Recupera el primero de los certificados del almacén.
     * </p>
     * 
     * @param storeManager Interfaz de acceso al almacén
     * @return Primer certificado disponible en el almacén
     */
    private X509Certificate getFirstCertificate(
            final IPKStoreManager storeManager) throws Exception {
        List certs = null;
        try {
            certs = storeManager.getSignCertificates();
        } catch (CertStoreException ex) {
            throw new Exception("Fallo obteniendo listado de certificados del cliente. "+ex.toString());
        }
        if ((certs == null) || (certs.size() == 0)) {
            throw new Exception("Lista de certificados de cliente vacía. ");
        }

        X509Certificate certificate = (X509Certificate)certs.get(0);
        return certificate;
    }
    
}
