package ec.gob.sri.debito.v1_0_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ec.gob.sri.JaxbTestFixture.config;

public class NotaDebitoTest {

    private static final String xmlFilename = "NotaDebito_V1.0.0.xml";
    private static final JaxbTestFixture<NotaDebito> testFixture = new JaxbTestFixture<>(NotaDebito.class, xmlFilename);

    @Test
    void loadValidNotaDebitoFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidNotaDebitoToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidNotaDebito_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidNotaDebitoConfigActions")
    void validateInvalidNotaDebito_raisesConstraintViolations(
            Consumer<NotaDebito> configAction,
            Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidNotaDebitoConfigActions() {
        return Stream.of(
                config("invalid id", NotaDebitoTest::invalidId, "must match \"\\Qcomprobante\\E\""),
                config("invalid version", NotaDebitoTest::invalidVersion, "NotaDebito.version must not be null"),
                config("missing infoTributaria", NotaDebitoTest::missingInfoTributaria, "NotaDebito.infoTributaria must not be null"),
                config("empty notaDebitoDetalles", NotaDebitoTest::emptyNotaDebitoMotivos, "size must be between 1 and 2147483647"),
                config("invalid notaDebitoDetalles", NotaDebitoTest::invalidNotaDebitoMotivos, "must match \"[^\\n]*\""),
                config("invalid valor", NotaDebitoTest::invalidValor, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(NotaDebito NotaDebito) {
        NotaDebito.setId("invalid");
    }

    private static void invalidVersion(NotaDebito NotaDebito) {
        NotaDebito.setVersion(null);
    }

    private static void missingInfoTributaria(NotaDebito notaDebito) {
        notaDebito.setInfoTributaria(null);
    }

    private static void emptyNotaDebitoMotivos(NotaDebito notaDebito) {
        notaDebito.getMotivos().getMotivo().clear();
    }

    private static void invalidNotaDebitoMotivos(NotaDebito notaDebito) {
        notaDebito.getMotivos().getMotivo().get(0).setRazon("XXX\nYYY");
    }

    private static void invalidValor(NotaDebito notaDebito) {
        notaDebito.getMotivos().getMotivo().get(0).setValor(BigDecimal.valueOf(50.1234));
    }
}
