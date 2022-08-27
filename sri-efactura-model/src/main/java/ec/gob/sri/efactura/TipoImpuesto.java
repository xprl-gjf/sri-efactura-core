package ec.gob.sri.efactura;

/**
 * Valores por el campo <tt>código</tt> por impuestos.
 *
 * <p>Según <a href="https://www.sri.gob.ec/o/sri-portlet-biblioteca-alfresco-internet/descargar/435ca226-b48d-4080-bb12-bf03a54527fd/FICHA%20TE%cc%81CNICA%20COMPROBANTES%20ELECTRO%cc%81NICOS%20ESQUEMA%20OFFLINE%20Versio%cc%81n%202.21.pdf">SRI Ficha técnica v2.21</a>,
 * Tabla 16.</p>
 */
public enum TipoImpuesto {
    IVA(2),
    ICE(3),
    IRBPNR(5);

    private final int code;

    TipoImpuesto(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }
}
