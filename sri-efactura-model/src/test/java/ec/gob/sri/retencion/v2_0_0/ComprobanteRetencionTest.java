package ec.gob.sri.retencion.v2_0_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ec.gob.sri.JaxbTestFixture.config;

public class ComprobanteRetencionTest {

    private static final String xmlFilename = "ComprobanteRetencion_V2.0.0.xml";
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
            config("invalid id", ComprobanteRetencionTest::invalidId, "must match \"\\Qcomprobante\\E\""),
            config("missing infoTributaria", ComprobanteRetencionTest::missingInfoTributaria, "ComprobanteRetencion.infoTributaria must not be null"),
            config("empty retencionImpuestos", ComprobanteRetencionTest::emptyDocsSustentos, "size must be between 1 and 2147483647"),
            config("invalid impuesto baseImponible", ComprobanteRetencionTest::invalidImpuestoBaseImponible, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(ComprobanteRetencion retencion) {
        retencion.setId("invalid");
    }

    private static void missingInfoTributaria(ComprobanteRetencion retencion) {
        retencion.setInfoTributaria(null);
    }

    private static void emptyDocsSustentos(ComprobanteRetencion retencion) {
        retencion.getDocsSustento().getDocSustento().clear();
    }

    private static void invalidImpuestoBaseImponible(ComprobanteRetencion retencion) {
        retencion.getDocsSustento().getDocSustento().get(0).getImpuestosDocSustento().getImpuestoDocSustento().get(0).setBaseImponible(BigDecimal.valueOf(500000000000000.12));
    }
}
