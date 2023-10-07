package ec.gob.sri.retencion.v1_0_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ComprobanteRetencionTest {

    private static final String xmlFilename = "ComprobanteRetencion_V1.0.0.xml";
    private static final JaxbTestFixture<ComprobanteRetencion> testFixture = new JaxbTestFixture<>(ComprobanteRetencion.class, xmlFilename);

    @Test
    void loadValidRetencionFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidRetencionToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidRetencion_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidRetencionConfigActions")
    void validateInvalidRetencion_raisesConstraintViolations(
        Consumer<ComprobanteRetencion> configAction,
        Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidRetencionConfigActions() {
        return Stream.of(
            JaxbTestFixture.config("invalid id", ComprobanteRetencionTest::invalidId, "must match \"\\Qcomprobante\\E\""),
            JaxbTestFixture.config("missing infoTributaria", ComprobanteRetencionTest::missingInfoTributaria, "ComprobanteRetencion.infoTributaria must not be null"),
            JaxbTestFixture.config("empty retencionImpuestos", ComprobanteRetencionTest::emptyRetencionImpuestos, "size must be between 1 and 2147483647"),
            JaxbTestFixture.config("invalid impuesto baseImponible", ComprobanteRetencionTest::invalidImpuestoBaseImponible, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(ComprobanteRetencion retencion) {
        retencion.setId("invalid");
    }

    private static void missingInfoTributaria(ComprobanteRetencion retencion) {
        retencion.setInfoTributaria(null);
    }

    private static void emptyRetencionImpuestos(ComprobanteRetencion retencion) {
        retencion.getImpuestos().getImpuesto().clear();
    }

    private static void invalidImpuestoBaseImponible(ComprobanteRetencion retencion) {
        retencion.getImpuestos().getImpuesto().get(0).setBaseImponible(BigDecimal.valueOf(500000000000000.12));
    }
}
