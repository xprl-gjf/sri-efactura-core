package ec.gob.sri;

import org.jetbrains.annotations.NotNull;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.params.provider.Arguments;

import jakarta.validation.ConstraintViolation;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import jakarta.validation.Validator;
import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public final class JaxbTestFixture<T> {

    private final String xmlResourceFilename;
    private final JaxbProvider<T> jaxbProvider;

    public JaxbTestFixture(@NotNull Class<T> clazz, @NotNull String xmlResourceFilename) {
        this.xmlResourceFilename = xmlResourceFilename;
        this.jaxbProvider = new JaxbProvider<>(clazz);
    }

    /**
     * Verify that a well-formed and valid XML file can be deserialized into the corresponding SRI object.
     */
    public void testLoadValidInstanceFromXml_doesNotThrow() {
        File xmlFile = getXmlResourceFile(xmlResourceFilename);
        assertDoesNotThrow(() -> {
            loadObjectFromXml(xmlFile);
        });
    }

    /**
     * Verify that a valid SRI object can be serialized to an XML file.
     */
    public void testWriteValidInstanceToXml_doesNotThrow() {
        File xmlFile = getXmlResourceFile(xmlResourceFilename);
        T obj = loadObjectFromXml(xmlFile);
        XMLStreamWriter writer = createXmlWriter();
        Marshaller marshaller = jaxbProvider.getMarhsaller();

        assertDoesNotThrow(() -> {
            marshaller.marshal(obj, writer);
            writer.flush();
        });
    }

    /**
     * Verify that running Jaxb validation on a valid SRI object does not raise validation errors.
     */
    public void testValidateValidInstance_raisesNoConstraintViolations() {
        File xmlFile = getXmlResourceFile(xmlResourceFilename);
        T obj = loadObjectFromXml(xmlFile);
        Validator validator = JaxbValidatorFactory.getValidator();

        Set<ConstraintViolation<T>> errors = validator.validate(obj);
        assertTrue(errors.isEmpty());
    }

    /**
     * Verify that running Jaxb validation on an invalid SRI object raises the corresponding validation errors.
     *
     * @param configAction action to perform to make the SRI object invalid.
     * @param expectedErrorMessages the expected validation error messages.
     */
    public void testValidateInvalidInstance_raisesConstraintViolations(
            @NotNull Consumer<T> configAction,
            @NotNull Iterable<String> expectedErrorMessages
    ) {
        File xmlFile = getXmlResourceFile(xmlResourceFilename);
        T obj = loadObjectFromXml(xmlFile);
        Validator validator = JaxbValidatorFactory.getValidator();

        configAction.accept(obj);
        Set<ConstraintViolation<T>> errors = validator.validate(obj);
        Iterable<String> messages = errors.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertIterableEquals(expectedErrorMessages, messages);
    }

    /**
     * Get the {@link File} representation of a resource using the current class loader.
     *
     * @param name the resource file name.
     * @return the {@link File} representation of the requested resource.
     */
    public File getXmlResourceFile(@NotNull String name) {
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

    /**
     * Create a new {@link XMLStreamWriter} that will write to a string.
     *
     * @return the newly created {@link XMLStreamWriter}.
     */
    public static XMLStreamWriter createXmlWriter() {
        StringWriter stringWriter = new StringWriter();
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try {
            return outputFactory.createXMLStreamWriter(stringWriter);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load an XML file and deserialize it to the corresponding SRI object type.
     *
     * @param xmlFile the file to load.
     * @return the deserialized SRI object.
     */
    @SuppressWarnings("unchecked")
    public T loadObjectFromXml(@NotNull File xmlFile) {
        try {
            Unmarshaller unmarshaller = jaxbProvider.getUnmarhsaller();
            Object obj = unmarshaller.unmarshal(xmlFile);
            return (T)obj;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenient utility function to generate JUnit arguments for an SRI object constraint violation test.
     *
     * @see #testValidateInvalidInstance_raisesConstraintViolations
     *
     * @param name the name to display for the test when run with these arguments.
     * @param configAction a config action to apply to the SRI object for this test.
     * @param errors the expected validation error messages.
     *
     * @return a JUnit {@link Arguments} instance.
     * @param <TConfig> the type of config action.
     */
    public static <TConfig> Arguments config(
            @NotNull String name,
            @NotNull Consumer<TConfig> configAction,
            @NotNull String... errors) {
        return arguments(named(name, configAction),
                Arrays.stream(errors).collect(Collectors.toList()));
    }
}

