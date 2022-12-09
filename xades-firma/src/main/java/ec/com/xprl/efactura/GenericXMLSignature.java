package ec.com.xprl.efactura;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import xades4j.XAdES4jException;
import xades4j.algorithms.CanonicalXMLWithoutComments;
import xades4j.production.*;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.KeyStoreKeyingDataProvider;

import static org.apache.xml.security.algorithms.MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1;
import static org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1;

public abstract class GenericXMLSignature {

    /*
     * Bug fix for https://bugs.openjdk.java.net/browse/JDK-8264194
     */
    static {
        // System.setProperty("com.sun.org.apache.xml.internal.security.ignoreLineBreaks", "true");
        System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
    }

    /**
     * Firmar los datos creados por el método abstracto <code>createDataToSign</code>
     * mediante el primer certificado encontré en el <code>firmaClienteKeyStore</code>.
     * El resultado del proceso de firma será devuelto como un DOM Document.
     * <p>
     * Uso este método cuando ni el KeyStore ni el certificado en el KeyStore
     * necesitan una contraseña.
     * </p>
     * @param archivo el contenido del documento para estar firmado.
     * @param firmaClienteKeyStore el KeyStore que contiene el certificado para la firma.
     * @param descripcion la descripción de los datos firmados (opcional).
     * @return el documento firmado.
     * @throws Exception el proceso de firmar el documento falló.
     */
    protected @NotNull Document execute(
            byte @NotNull [] archivo,
            @NotNull File firmaClienteKeyStore,
            String descripcion) throws Exception {
        return execute(
            archivo, firmaClienteKeyStore, null, null, descripcion
        );
    }

    /**
     * Firmar los datos creados por el método abstracto <code>createDataToSign</code>
     * mediante el primer certificado encontré en el <code>firmaClienteKeyStore</code>.
     * El resultado del proceso de firma será devuelto como un DOM Document.
     * <p>
     * Uso este método cuando una contraseña es necesario para abrir el KeyStore,
     * pero no hay contraseña por el certificado en el KeyStore.
     * </p>
     * @param archivo el contenido del documento para estar firmado.
     * @param firmaClienteKeyStore el KeyStore que contiene el certificado para la firma.
     * @param keyStoreClave la contraseña para abrir el KeyStore.
     * @param descripcion la descripción de los datos firmados (opcional).
     * @return el documento firmado.
     * @throws Exception el proceso de firmar el documento falló.
     */
    protected @NotNull Document execute(
            byte @NotNull [] archivo,
            @NotNull File firmaClienteKeyStore,
            String keyStoreClave,
            String descripcion) throws Exception {
        return execute(
            archivo, firmaClienteKeyStore, keyStoreClave, null, descripcion
        );
    }

    /**
     * Firmar los datos creados por el método abstracto <code>createDataToSign</code>
     * mediante el primer certificado encontré en el <code>firmaClienteKeyStore</code>.
     * El resultado del proceso de firma será devuelto como un DOM Document.
     * <p>
     * Uso este método cuando una contraseña es necesario para abrir el KeyStore,
     * pero no hay contraseña por el certificado en el KeyStore.
     * </p>
     * @param archivo el contenido del documento para estar firmado.
     * @param firmaClienteKeyStore el KeyStore que contiene el certificado para la firma.
     * @param keyStoreClave la contraseña para abrir el KeyStore.
     * @param keyStoreEntryClave la contraseña por el certificado.
     * @param descripcion la descripción de los datos firmados (opcional).
     * @return el documento firmado.
     * @throws Exception el proceso de firmar el documento falló.
     */
    protected Document execute(
            byte @NotNull [] archivo,
            @NotNull File firmaClienteKeyStore,
            String keyStoreClave,
            @SuppressWarnings("SameParameterValue")
            String keyStoreEntryClave,
            String descripcion) throws Exception {

        // Obtencion del gestor de claves
        KeyingDataProvider kp = getKeyingDataProvider(firmaClienteKeyStore, keyStoreClave, keyStoreEntryClave);

        // Obtencion del firmador
        XadesSigningProfile p = getXadesSigningProfile(kp)
                .withSignatureAlgorithms(new SignatureAlgorithms()
                    .withSignatureAlgorithm("RSA", ALGO_ID_SIGNATURE_RSA_SHA1)
                    .withDigestAlgorithmForDataObjectReferences(ALGO_ID_DIGEST_SHA1)
                    .withCanonicalizationAlgorithmForSignature(new CanonicalXMLWithoutComments())
                    .withDigestAlgorithmForReferenceProperties(ALGO_ID_DIGEST_SHA1))
                .withBasicSignatureOptions(new BasicSignatureOptions()
                    .signKeyInfo(true)
                    .includePublicKey(true)
                    .includeSigningCertificate(SigningCertificateMode.SIGNING_CERTIFICATE)
                );

        /*
         * Creación del objeto que contiene tanto los datos a firmar como la
         * configuración del tipo de firma
         */
        Document doc = getDocument(archivo);
        SignedDataObjects dataToSign = createDataToSign(doc, descripcion);
        Element sigParentNode = getSignatureParentNode(doc);
        if (sigParentNode == null) {
            throw new IllegalStateException("Documento para firmar no contiene el node descendiente que es necesario.");
        }

        try {
            /*
             * Creación del objeto encargado de realizar la firma
             */
            XadesSigner signer = p.newSigner();
            signer.sign(dataToSign, sigParentNode);
        } catch (XAdES4jException ex) {
            ex.printStackTrace();
            throw new Exception("Error realizando la firma. ", ex);
        }
        return doc;
    }

    /**
     * Generar una implementación del XadesSigningProfile con el KeyingDataProvider
     * suministrado.
     * <p>
     * Todas las implementaciones deberán proporcionar una implementación de este método
     * </p>
     *
     * @param kp el KeyingDataProvider.
     * @return una implementación del XadesSigningProfile.
     */
    protected abstract @NotNull XadesSigningProfile getXadesSigningProfile(KeyingDataProvider kp);

    /**
     * <p>
     * Crea el objeto DataToSign que contiene toda la información de la firma
     * que se desea realizar. Todas las implementaciones deberán proporcionar
     * una implementación de este método
     * </p>
     * 
     * @return El objeto SignedDataObjects que contiene toda la información de la firma
     *         a realizar
     */
    protected abstract @NotNull SignedDataObjects createDataToSign(@NotNull Document document, String descripcion) throws Exception;

    /**
     * Seleccionar el elemento en el documento en que la firma va a estar adjuntado.
     * <p>
     * Todas las implementaciones deberán proporcionar una implementación de este método
     * </p>
     *
     * @param document el documento para que el elemento va a estar seleccionado.
     * @return un elemento, o null si no elemento está valido.
     */
    protected abstract Element getSignatureParentNode(@NotNull Document document);

    /**
     * <p>
     * Devuelve el <code>Document</code> correspondiente al
     * <code>resource</code> pasado como parámetro
     * </p>
     * 
     * @param resource El recurso que se desea obtener
     * @return El <code>Document</code> asociado al <code>resource</code>
     */
    protected @NotNull Document getDocument(byte @NotNull [] resource) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
        	InputStream b = new ByteArrayInputStream(resource);
            return dbf.newDocumentBuilder().parse(b);
        } catch (ParserConfigurationException
                 | SAXException
                 | IOException
                 | IllegalArgumentException ex)
        {
            ex.printStackTrace();
            throw new Exception("Error al parsear el documento. ", ex);
        }
    }

    /**
     * <p>
     * Devuelve el gestor de claves que se va a utilizar
     * </p>
     * 
     * @return El gestor de claves que se va a utilizar</p>
     */
    private @NotNull KeyingDataProvider getKeyingDataProvider(@NotNull File file, String fileClave, String entryClave) throws Exception {

        FileSystemKeyStoreKeyingDataProvider.Builder builder = FileSystemKeyStoreKeyingDataProvider
            .builder("pkcs12", file.getPath(), GenericXMLSignature::SelectFirstCertificate)
            .fullChain(true);

        if (fileClave != null) {
            builder.storePassword(new DirectPasswordProvider(fileClave));
        } else {
            builder.storePassword(new DirectPasswordProvider(""));
        }

        if (entryClave != null) {
            builder.entryPassword(new DirectPasswordProvider(entryClave));
        } else {
            builder.entryPassword(new DirectPasswordProvider(""));
        }
        return builder.build();
    }

    private static @NotNull KeyStoreKeyingDataProvider.SigningCertificateSelector.Entry SelectFirstCertificate(
        @NotNull List<KeyStoreKeyingDataProvider.SigningCertificateSelector.Entry> entries
    ) {
        Optional<KeyStoreKeyingDataProvider.SigningCertificateSelector.Entry> result = entries.stream()
                .filter(Objects::nonNull)
                .findFirst();
        if (!result.isPresent()) {
            throw new IllegalStateException("No existe ningún certificado para firmar.");
        }
        return result.get();
    }
}
