package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.seguridad.dto.ValidacionDto;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
//import ec.gob.dinardap.turno.modelo.RegistroMercantil;
//import ec.gob.dinardap.turno.modelo.Usuario;
//import ec.gob.dinardap.turno.servicio.RegistroMercantilServicio;
//import ec.gob.dinardap.turno.servicio.UsuarioServicio;

@Named(value = "restaurarClaveCtrl")
@ViewScoped
public class RestaurarClaveCtrl extends BaseCtrl {
    
    private static final long serialVersionUID = 4873444362560469061L;
    
    private String nombreUsuario;
    private String correoElectronico;
    
    @EJB
    UsuarioServicio usuarioServicio;
    
    @PostConstruct
    public void init() {
    }
    
    public void restaurarClave() {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getNombreUsuario());
        if (usuario != null) {
            if (usuario.getCorreoElectronico().equals(getCorreoElectronico())) {
                usuario.setContrasena(EncriptarCadenas.encriptarCadenaSha1(GeneradorCodigo.generarCodigo(12)));
                                
                usuarioServicio.update(usuario);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
        }
    }

    //Getters & Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
}
