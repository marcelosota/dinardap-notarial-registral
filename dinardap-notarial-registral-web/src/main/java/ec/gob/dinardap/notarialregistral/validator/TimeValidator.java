/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.validator;

import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;

/**
 *
 * @author christian.gaona
 */
public class TimeValidator implements Validator<Object>, ClientValidator {

    private Pattern pattern;

    private static final String HOUR_PATTERN = "([0-1][0-9]|2[0-3]):[0-5][0-9]";

    public TimeValidator() {
        pattern = Pattern.compile(HOUR_PATTERN);
    }

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        if (!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Hora: Error de Validación: " + value + ", no es una hora correcta.",
                    "Is not a valid hour format"));
        }
    }

    public Map<String, Object> getMetadata() {
        return null;
    }

    public String getValidatorId() {
        return "timeValidator";
    }
}
