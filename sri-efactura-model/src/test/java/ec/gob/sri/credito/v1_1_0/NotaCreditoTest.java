package ec.gob.sri.credito.v1_1_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ec.gob.sri.JaxbTestFixture.config;

public class NotaCreditoTest {

    private static final String xmlFilename = "NotaCredito_V1.1.0.xml";
    private static final JaxbTestFixture<NotaCredito> testFixture = new JaxbTestFixture<>(NotaCredito.class, xmlFilename);

    @Test
    void loadValidNotaCreditoFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidNotaCreditoToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidNotaCredito_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidNotaCreditoConfigActions")
    void validateInvalidNotaCredito_raisesConstraintViolations(
            Consumer<NotaCredito> configAction,
            Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidNotaCreditoConfigActions() {
        return Stream.of(
                config("invalid id", NotaCreditoTest::invalidId, "must match \"\\Qcomprobante\\E\""),
                config("missing infoTributaria", NotaCreditoTest::missingInfoTributaria, "NotaCredito.infoTributaria must not be null"),
                config("empty notaCreditoDetalles", NotaCreditoTest::emptyNotaCreditoDetalles, "size must be between 1 and 2147483647"),
                config("invalid notaCreditoDetalles", NotaCreditoTest::invalidNotaCreditoDetalles, "must match \"[^\\n]*\""),
                config("invalid valorModificacion", NotaCreditoTest::invalidValorModificacion, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(NotaCredito notaCredito) {
        notaCredito.setId("invalid");
    }

    private static void missingInfoTributaria(NotaCredito notaCredito) {
        notaCredito.setInfoTributaria(null);
    }

    private static void emptyNotaCreditoDetalles(NotaCredito notaCredito) {
        notaCredito.getDetalles().getDetalle().clear();
    }

    private static void invalidNotaCreditoDetalles(NotaCredito notaCredito) {
        notaCredito.getDetalles().getDetalle().get(0).setDescripcion("XXX\nYYY");
    }

    private static void invalidValorModificacion(NotaCredito notaCredito) {
        notaCredito.getInfoNotaCredito().setValorModificacion(BigDecimal.valueOf(50.1234));
    }
}
