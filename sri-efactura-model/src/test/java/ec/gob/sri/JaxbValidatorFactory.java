package ec.gob.sri;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class JaxbValidatorFactory {
    private static class Instance {
        // INSTANCE is initialized on demand when Instance class is first accessed
        private static final ValidatorFactory INSTANCE = Validation.buildDefaultValidatorFactory();
    }

    public static Validator getValidator() {
        return Instance.INSTANCE.getValidator();
    }

}

