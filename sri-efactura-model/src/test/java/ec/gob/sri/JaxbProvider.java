package ec.gob.sri;

import jakarta.xml.bind.*;

public class JaxbProvider<T> {

    private final Class<T> clazz;
    private JAXBContext context = null;
    private Marshaller marshaller = null;
    private Unmarshaller unmarshaller = null;

    public JaxbProvider(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Marshaller getMarhsaller() {
        if (marshaller == null) {
            try {
                marshaller = getContext().createMarshaller();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return marshaller;
    }


    public Unmarshaller getUnmarhsaller() {
        if (unmarshaller == null) {
            try {
                unmarshaller = getContext().createUnmarshaller();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return unmarshaller;
    }

    private JAXBContext getContext() {
        if (context == null) {
            context = createContext();
        }
        return context;
    }

    private JAXBContext createContext() {
        try {
            context = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return context;
    }
}
