package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.seguridad.dto.ValidacionDto;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
//import ec.gob.dinardap.turno.modelo.RegistroMercantil;
//import ec.gob.dinardap.turno.modelo.Usuario;
//import ec.gob.dinardap.turno.servicio.RegistroMercantilServicio;
//import ec.gob.dinardap.turno.servicio.UsuarioServicio;

@Named(value = "loginCtrl")
@ViewScoped
public class LoginCtrl extends BaseCtrl {

    private static final long serialVersionUID = 4873444362560469061L;

    @EJB
    private UsuarioServicio usuarioServicio;

    private String nombreUsuario;
    private String contrasena;

    private ValidacionDto validacionDto;

    @PostConstruct
    public void init() {
    }

    public void validarUsuario() {
        validacionDto = new ValidacionDto();
        validacionDto = usuarioServicio.validarUsuario(getNombreUsuario(),
                EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getContrasena())),
                Integer.parseInt(getIdentificacionSistema()));
        if (validacionDto != null) {
            setSessionVariable("perfil", validacionDto.getPerfil());
            //setSessionVariable("perfil", String.join(",", validacionDto.getPerfil().toString()));
            //setSessionVariable("institucionId", String.join(",", validacionDto.getInstitucionId().toString()));
            setSessionVariable("usuarioId", validacionDto.getUsuarioId().toString());
            setSessionVariable("institucionId", validacionDto.getInstitucion());
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                limpiarCampos();
                context.redirect(context.getRequestContextPath() + "/paginas/brand.jsf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
        }
    }

    public void cancelar() {
        setNombreUsuario(null);
        setContrasena(null);
    }

    public void recuperarClave() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/restaurarClave.jsf");
        } catch (IOException ex) {
            Logger.getLogger(LoginCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirDialogo() {

    }

    public void cambiarContrasena() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            //limpiarCampos();
            context.redirect(context.getRequestContextPath() + "/publico/cambiarContrasena.jsf");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        limpiarCampos();
//        setDialogo(Boolean.FALSE);
    }

    public void enviar() {

        /*try {
			Email email = new Email();
			String mensajeMail = "Prueba";
			email.sendMail("leonardo.munoz@dinardap.gob.ec", "Consulta", mensajeMail);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

    private void limpiarCampos() {
//        setNuevaContrasena("");
//        setContrasena("");
//        setCedula("");
//        setEntidad(null);
    }

    // Getters & Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Integer getEntidad() {
//        return entidad;
        return 0;
    }

    public void setEntidad(Integer entidad) {
//        this.entidad = entidad;
    }

}
