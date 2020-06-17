package ec.gob.dinardap.notarialregistral.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.correo.util.MailMessage;
import ec.gob.dinardap.notarialregistral.util.Credenciales;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;

@Named(value = "restaurarClaveCtrl")
@ViewScoped
public class RestaurarClaveCtrl extends BaseCtrl {

    private static final long serialVersionUID = 4873444362560469061L;

    private String nombreUsuario;
    private String correoElectronico;

    @EJB
    UsuarioServicio usuarioServicio;

    @EJB
    private ClienteQueueMailServicio clienteQueueMailServicio;

    @PostConstruct
    public void init() {
    }

    public void restaurarClave() {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getNombreUsuario());
        if (usuario != null) {
            if (usuario.getCorreoElectronico().equals(getCorreoElectronico())) {
                usuario.setContrasena(EncriptarCadenas.encriptarCadenaSha1(GeneradorCodigo.generarCodigo(12)));
                usuario.setFechaModificacion(new Date());
                System.out.println("Enviar Correo");

                String parametroAmbiente = "DESARROLLO";
                MailMessage mailMessage = new MailMessage();

                StringBuilder html = new StringBuilder(200);
                html.append("<br />Estimado/a: <br />");
                html.append("<br /><br />Su nueva contraseña temporal es: ");
                html.append(usuario.getContrasena());
                html.append("<br/>");
                html.append("<br/>Favor actualizarla una vez autenticado en el sistema<br/>");
                html.append("<br/>Atentamente,<br/>");
                html.append("<br/><FONT COLOR=\"#0000ff\" FACE=\"Arial Narrow, sans-serif\"><B> ");
                html.append("<br/>");
                html.append("SANYR");
                html.append("</B></FONT>");

                List<String> to = new ArrayList<String>();
                StringBuilder asunto = new StringBuilder(200);

                if (parametroAmbiente.equals("PRODUCCION")) {

                } else {
                    to.add(usuario.getCorreoElectronico());
                    asunto.append("Notificación SANYR");
                }
//        asunto.append("Confirmación de solicitud para categorizar a la empresa: ");
                mailMessage = new Credenciales().credencialesCorreo();
                mailMessage.setTo(to);
                mailMessage.setSubject(asunto.toString());
                mailMessage.setText(html.toString());
                //mailServicio.sender(mailMessage);
                clienteQueueMailServicio.encolarMail(mailMessage);

                usuarioServicio.update(usuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Recuperación de Contraseña", getBundleMensaje("mensaje.restauracionClave", null)));
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
