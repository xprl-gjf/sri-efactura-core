package uk.co.xprl.efactura;

import es.mityc.javasign.xml.refs.AbstractObjectToSign;

public class XMLComprobante extends AbstractObjectToSign {
	
    public XMLComprobante() {
//		addTransform(new TransformEnveloped());
    }
	
    public String getReferenceURI() {
            return "#comprobante";
    }
    
}
