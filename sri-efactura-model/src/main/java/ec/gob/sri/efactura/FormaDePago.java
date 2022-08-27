package ec.gob.sri.efactura;

/**
 * Valores por el campo <tt>formaPago</tt>.
 *
 * <p>Según <a href="https://www.sri.gob.ec/o/sri-portlet-biblioteca-alfresco-internet/descargar/435ca226-b48d-4080-bb12-bf03a54527fd/FICHA%20TE%cc%81CNICA%20COMPROBANTES%20ELECTRO%cc%81NICOS%20ESQUEMA%20OFFLINE%20Versio%cc%81n%202.21.pdf">SRI Ficha técnica v2.21</a>,
 * Tabla 24.</p>
 */
public enum FormaDePago {
    SIN_SISTEMA_FINANCIERO(1),
    COMPENSACION_DE_DEUDAS(15),
    TARJETA_DE_DEBITO(16),
    DINERO_ELECTRONICO(17),
    TARJETA_PREPAGO(18),
    TARJETA_DE_CREDITO(19),
    OTROS_CON_SISTEMA_FINANCIERO(20),
    ENDOSO_DE_TITULOS(21);

    private final int code;

    FormaDePago(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }
}
