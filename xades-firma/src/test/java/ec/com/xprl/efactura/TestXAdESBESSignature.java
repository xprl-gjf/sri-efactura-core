package ec.com.xprl.efactura;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;

public class TestXAdESBESSignature {

    private final String firmaFilename = "example.p12";
    private final String firmaClave = "password";

    public TestXAdESBESSignature() {
        configureJavaSecurity();
    }

    /**
     * Verify that signing a Factura XML with a known signature
     * gives the expected result
     */
    @ParameterizedTest()
    @CsvSource({"factura.xml,factura.signed.xml"})
    public void testSignedFacturaXml(
            @NotNull String facturaFilename,
            @NotNull String expectedResultFilename
    ) throws Exception {
        File xmlFile = getResourceFile(facturaFilename);
        File expectedResultFile = getResourceFile(expectedResultFilename);
        File firma = getResourceFile(firmaFilename);

        byte[] xmlContent = readFile(xmlFile.getPath());
        XAdESBESSignature firmador = new XAdESBESSignature();

        Document result = firmador.firmarDocumento(
                xmlContent,
                firma,
                firmaClave,
                "contenido comprobante");

        saveDocumentToFile(result, "result.xml");
        XmlAssert.assertThat(result).and(Input.fromFile(expectedResultFile))
                .ignoreWhitespace()
                .withNodeFilter(TestXAdESBESSignature::FacturaXmlNodeComparisonFilter)
                .withAttributeFilter(TestXAdESBESSignature::FacturaXmlAttributeComparisonFilter)
                .areSimilar();
    }

    /**
     * Verify that signing a modified Factura XML results in
     * a signature that differs from the expected result.
     */
    @ParameterizedTest()
    @CsvSource({"factura.modified.xml,factura.signed.xml"})
    public void testSignedModifiedFacturaXml(
            @NotNull String facturaFilename,
            @NotNull String expectedResultFilename
    ) throws Exception {
        File xmlFile = getResourceFile(facturaFilename);
        File expectedResultFile = getResourceFile(expectedResultFilename);
        File firma = getResourceFile(firmaFilename);

        byte[] xmlContent = readFile(xmlFile.getPath());
        XAdESBESSignature firmador = new XAdESBESSignature();

        Document result = firmador.firmarDocumento(
                xmlContent,
                firma,
                firmaClave,
                "contenido comprobante");

        // firmador.saveDocumentToFile(result, "result.xml");
        XmlAssert.assertThat(result).and(Input.fromFile(expectedResultFile))
                .ignoreWhitespace()
                .withNodeFilter(TestXAdESBESSignature::FacturaXmlNodeComparisonFilter)
                .withAttributeFilter(TestXAdESBESSignature::FacturaXmlAttributeComparisonFilter)
                .areNotSimilar();
    }

    private static boolean FacturaXmlNodeComparisonFilter(Node node) {
        String name = node.getLocalName();
        if (name == null) {
            return true;
        }
        return !name.equalsIgnoreCase("SigningTime")
            && !name.equalsIgnoreCase("SignatureValue")
            && !name.equalsIgnoreCase("DigestValue");
    }

    private static boolean FacturaXmlAttributeComparisonFilter(Attr attr) {
        String nodeName = attr.getOwnerElement().getLocalName();
        String name = attr.getName();
        if (name == null) {
            return true;
        }
        return !name.equalsIgnoreCase("Id")
            && !(nodeName.equalsIgnoreCase("Reference") && name.equalsIgnoreCase("URI"))
            && !(nodeName.equalsIgnoreCase("QualifyingProperties") && name.equalsIgnoreCase("Target"))
            && !(nodeName.equalsIgnoreCase("DataObjectFormat") && name.equalsIgnoreCase("ObjectReference"));
    }

    private static void configureJavaSecurity() {
        /* install the BouncyCastle security provider, needed for signing comprobante XML */
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * Get the {@link File} representation of a resource using the current class loader.
     *
     * @param name the resource file name.
     * @return the {@link File} representation of the requested resource.
     */
    private File getResourceFile(@NotNull String name) {
        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private static byte[] readFile(String path)
        throws IOException
    {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * <p>
     * Escribe el documento a un fichero.
     * </p>
     *
     * @param document El documento a imprmir
     * @param filePath El path del fichero donde se quiere escribir.
     */
    private static void saveDocumentToFile(Document document, String filePath) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        FileWriter writer = new FileWriter(filePath);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
    }
}
