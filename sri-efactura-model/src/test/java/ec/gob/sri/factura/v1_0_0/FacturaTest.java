package ec.gob.sri.factura.v1_0_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ec.gob.sri.JaxbTestFixture.config;

public class FacturaTest {

    private static final String xmlFilename = "factura_V1.0.0.xml";
    private static final JaxbTestFixture<Factura> testFixture = new JaxbTestFixture<>(Factura.class, xmlFilename);

    @Test
    void loadValidFacturaFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidFacturaToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidFactura_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidFacturaConfigActions")
    void validateInvalidFactura_raisesConstraintViolations(
        Consumer<Factura> configAction,
        Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidFacturaConfigActions() {
        return Stream.of(
            config("invalid id", FacturaTest::invalidId, "must match \"\\Qcomprobante\\E\""),
            config("missing infoTributaria", FacturaTest::missingInfoTributaria, "Factura.infoTributaria must not be null"),
            config("empty facturaDetalles", FacturaTest::emptyFacturaDetalles, "size must be between 1 and 2147483647"),
            config("invalid facturaDetalles", FacturaTest::invalidFacturaDetalles, "size must be between 1 and 25"),
            config("invalid totalDescuento", FacturaTest::invalidTotalDescuento, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(Factura factura) {
        factura.setId("invalid");
    }

    private static void missingInfoTributaria(Factura factura) {
        factura.setInfoTributaria(null);
    }

    private static void emptyFacturaDetalles(Factura factura) {
        factura.getDetalles().getDetalle().clear();
    }

    private static void invalidFacturaDetalles(Factura factura) {
        factura.getDetalles().getDetalle().get(0).setCodigoPrincipal("");
    }

    private static void invalidTotalDescuento(Factura factura) {
        factura.getInfoFactura().setTotalDescuento(BigDecimal.valueOf(50.1234));
    }
}
