/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.util;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import ec.gob.dinardap.seguridad.servicio.ParametroServicio;

/**
 *
 * @author christian.gaona
 */
@Local
@Stateless(name="Credenciales")
public class Credenciales {

    @EJB
    ParametroServicio parametroServicio;

    /*public MailMessage credencialesCorreo() {
        MailMessage credenciales = new MailMessage();
        credenciales.setFrom(parametroServicio.findByPk(ParametroEnum.MAIL_SANYR.name()).getValor());
        credenciales.setUsername(parametroServicio.findByPk(ParametroEnum.MAIL_USERNAME_SANYR.name()).getValor());
        credenciales.setPassword(parametroServicio.findByPk(ParametroEnum.MAIL_CONTRASENA_SANYR.name()).getValor());
        return credenciales;
    }*/

}
