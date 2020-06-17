/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.util;

import ec.gob.dinardap.correo.util.MailMessage;
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import javax.ejb.EJB;

/**
 *
 * @author christian.gaona
 */
public class Credenciales {

    @EJB
    ParametroServicio parametroServicio;

    public MailMessage credencialesCorreo() {
        MailMessage credenciales = new MailMessage();
        credenciales.setFrom(parametroServicio.findByPk(ParametroEnum.MAIL.name()).getValor());
        credenciales.setUsername(parametroServicio.findByPk(ParametroEnum.MAIL_USERNAME.name()).getValor());
        credenciales.setPassword(parametroServicio.findByPk(ParametroEnum.MAIL_PASSWORD.name()).getValor());
        return credenciales;
    }

}
