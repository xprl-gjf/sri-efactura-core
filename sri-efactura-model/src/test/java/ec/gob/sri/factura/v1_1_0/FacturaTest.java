package ec.gob.sri.factura.v1_1_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FacturaTest {

    private static final String xmlFilename = "factura_V1.1.0.xml";
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
            JaxbTestFixture.config("invalid version", FacturaTest::invalidRazonSocial, "size must be between 1 and 300"),
            JaxbTestFixture.config("missing infoTributaria", FacturaTest::missingInfoFactura, "Factura.infoFactura must not be null"),
            JaxbTestFixture.config("empty facturaDetalles", FacturaTest::emptyFacturaPagos, "size must be between 1 and 2147483647"),
            JaxbTestFixture.config("invalid totalSinImpuestos", FacturaTest::invalidTotalSinImpuestos, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidRazonSocial(Factura factura) {
        factura.getInfoTributaria().setRazonSocial("");
    }

    private static void missingInfoFactura(Factura factura) {
        factura.setInfoFactura(null);
    }

    private static void emptyFacturaPagos(Factura factura) {
        factura.getInfoFactura().getPagos().getPago().clear();
    }

    private static void invalidTotalSinImpuestos(Factura factura) { factura.getInfoFactura().setTotalSinImpuestos(BigDecimal.valueOf(50.123)); }
}
