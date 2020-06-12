/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.converter;

import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.notarialregistral.servicio.TipoTramiteServicio;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author enery
 */
@SuppressWarnings("rawtypes")
@ManagedBean
@Named(value = "tipoTramiteConverter")
public class TipoTramiteConverter implements Converter {
    

    @EJB
    private TipoTramiteServicio tipoTramiteServicio;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        TipoTramite tt = new TipoTramite();
        if (value != null && value.trim().length() > 0) {
            try {
                tt = tipoTramiteServicio.findByPk(Integer.parseInt(value));
                return tt;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Tipo de Trámite"));
            }
        } else {
            return tt;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((TipoTramite) object).getTipoTramiteId());
        } else {
            return null;
        }
    }
}
