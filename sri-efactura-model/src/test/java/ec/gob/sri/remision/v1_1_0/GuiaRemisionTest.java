package ec.gob.sri.remision.v1_1_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class GuiaRemisionTest {

    private static final String xmlFilename = "GuiaRemision_V1.0.0.xml";
    private static final JaxbTestFixture<GuiaRemision> testFixture = new JaxbTestFixture<>(GuiaRemision.class, xmlFilename);

    @Test
    void loadValidRemisionFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidRemisionToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidRemision_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidRemisionConfigActions")
    void validateInvalidRemision_raisesConstraintViolations(
        Consumer<GuiaRemision> configAction,
        Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidRemisionConfigActions() {
        return Stream.of(
            JaxbTestFixture.config("invalid id", GuiaRemisionTest::invalidId, "must match \"\\Qcomprobante\\E\""),
            JaxbTestFixture.config("missing infoTributaria", GuiaRemisionTest::missingInfoTributaria, "GuiaRemision.infoTributaria must not be null"),
            JaxbTestFixture.config("empty remision destinatarios", GuiaRemisionTest::emptyRemisionDestinatarios, "size must be between 1 and 2147483647"),
            JaxbTestFixture.config("empty razonSocialTransportista", GuiaRemisionTest::infoGuiaRemisionInvalidRazonSocialTransportista, "size must be between 1 and 300"),
            JaxbTestFixture.config("invalid detalle descripcion", GuiaRemisionTest::invalidDetalleDescripcion, "size must be between 1 and 25")
        );
    }

    private static void invalidId(GuiaRemision remision) {
        remision.setId("invalid");
    }

    private static void missingInfoTributaria(GuiaRemision remision) {
        remision.setInfoTributaria(null);
    }

    private static void emptyRemisionDestinatarios(GuiaRemision remision) {
        remision.getDestinatarios().getDestinatario().clear();
    }

    private static void infoGuiaRemisionInvalidRazonSocialTransportista(GuiaRemision remision) {
        remision.getInfoGuiaRemision().setRazonSocialTransportista("");
    }

    private static void invalidDetalleDescripcion(GuiaRemision remision) {
        remision.getDestinatarios().getDestinatario().get(0).getDetalles().getDetalle().get(0).setCodigoInterno("");
    }
}
