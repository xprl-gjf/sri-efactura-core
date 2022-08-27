package ec.gob.sri.efactura;

/**
 * Valores por el campo <tt>codDoc</tt>.
 *
 * <p>Según <a href="https://www.sri.gob.ec/o/sri-portlet-biblioteca-alfresco-internet/descargar/435ca226-b48d-4080-bb12-bf03a54527fd/FICHA%20TE%cc%81CNICA%20COMPROBANTES%20ELECTRO%cc%81NICOS%20ESQUEMA%20OFFLINE%20Versio%cc%81n%202.21.pdf">SRI Ficha técnica v2.21</a>,
 * Tabla 3.</p>
 */
public enum DocumentType {
    FACTURA(1),
    LIQUIDACION_DE_COMPRA(3),
    NOTA_DE_CREDITO(4),
    NOTA_DE_DEBITO(5),
    GUIA_DE_REMISION(6),
    COMPROBANTE_DE_RETENCION(7);

    private final int code;

    DocumentType(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }
}
