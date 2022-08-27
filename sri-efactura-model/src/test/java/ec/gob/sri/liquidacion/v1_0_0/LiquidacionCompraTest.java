package ec.gob.sri.liquidacion.v1_0_0;

import ec.gob.sri.JaxbTestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ec.gob.sri.JaxbTestFixture.config;

public class LiquidacionCompraTest {

    private static final String xmlFilename = "LiquidacionCompra_V1.0.0.xml";
    private static final JaxbTestFixture<LiquidacionCompra> testFixture = new JaxbTestFixture<>(LiquidacionCompra.class, xmlFilename);

    @Test
    void loadValidLiquidacionCompraFromXml_doesNotThrow() {
        testFixture.testLoadValidInstanceFromXml_doesNotThrow();
    }

    @Test
    void writeValidLiquidacionCompraToXml_doesNotThrow() {
        testFixture.testWriteValidInstanceToXml_doesNotThrow();
    }

    @Test
    void validateValidLiquidacionCompra_raisesNoConstraintViolations() {
        testFixture.testValidateValidInstance_raisesNoConstraintViolations();
    }

    @ParameterizedTest
    @MethodSource("getInvalidLiquidacionCompraConfigActions")
    void validateInvalidLiquidacionCompra_raisesConstraintViolations(
            Consumer<LiquidacionCompra> configAction,
            Iterable<String> expectedErrorMessages
    ) {
        testFixture.testValidateInvalidInstance_raisesConstraintViolations(configAction, expectedErrorMessages);
    }

    private static Stream<Arguments> getInvalidLiquidacionCompraConfigActions() {
        return Stream.of(
                config("invalid id", LiquidacionCompraTest::invalidId, "must match \"\\Qcomprobante\\E\""),
                config("missing infoTributaria", LiquidacionCompraTest::missingInfoTributaria, "LiquidacionCompra.infoTributaria must not be null"),
                config("empty liquidacionCompraDetalles", LiquidacionCompraTest::emptyLiquidacionCompraDetalles, "size must be between 1 and 2147483647"),
                config("invalid liquidacionCompraDetalles", LiquidacionCompraTest::invalidLiquidacionCompraDetalles, "size must be between 1 and 25"),
                config("invalid totalDescuento", LiquidacionCompraTest::invalidTotalDescuento, "numeric value out of bounds (<14 digits>.<2 digits> expected)")
        );
    }

    private static void invalidId(LiquidacionCompra LiquidacionCompra) {
        LiquidacionCompra.setId("invalid");
    }

    private static void missingInfoTributaria(LiquidacionCompra LiquidacionCompra) {
        LiquidacionCompra.setInfoTributaria(null);
    }

    private static void emptyLiquidacionCompraDetalles(LiquidacionCompra LiquidacionCompra) {
        LiquidacionCompra.getDetalles().getDetalle().clear();
    }

    private static void invalidLiquidacionCompraDetalles(LiquidacionCompra LiquidacionCompra) {
        LiquidacionCompra.getDetalles().getDetalle().get(0).setCodigoPrincipal("");
    }

    private static void invalidTotalDescuento(LiquidacionCompra LiquidacionCompra) {
        LiquidacionCompra.getInfoLiquidacionCompra().setTotalDescuento(BigDecimal.valueOf(50.1234));
    }
}
