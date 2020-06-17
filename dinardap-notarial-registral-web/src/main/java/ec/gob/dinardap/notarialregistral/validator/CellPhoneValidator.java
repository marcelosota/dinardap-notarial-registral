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
public class CellPhoneValidator implements Validator<Object>, ClientValidator {

    private Pattern pattern;

    private static final String PHONE_PATTERN = "(\\+593|593|0)[ -]*([0-9]){2}[ -]*([0-9]){3}[ -]*([0-9]){4}[ -]*";

    public CellPhoneValidator() {
        pattern = Pattern.compile(PHONE_PATTERN);
    }

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        if (!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Celular: Error de Validación: " + value + ", no es un número de celular válido.",
                    "Is not a valid email"));
        }
    }

    public Map<String, Object> getMetadata() {
        return null;
    }

    public String getValidatorId() {
        return "cellPhoneValidator";
    }
}
