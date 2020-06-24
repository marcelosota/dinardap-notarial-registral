package ec.gob.dinardap.notarialregistral.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.correo.util.MailMessage;
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
//import ec.gob.dinardap.notarialregistral.util.Email;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;

@Named(value = "restaurarClaveCtrl")
@ViewScoped
public class RestaurarClaveCtrl extends BaseCtrl {

	private static final long serialVersionUID = 4873444362560469061L;

	private String nombreUsuario;
	private String correoElectronico;

	@EJB
	private UsuarioServicio usuarioServicio;

	@EJB
	private ClienteQueueMailServicio clienteQueueMailServicio;


	//@EJB
	//private MailServicio mailServicio;

	@EJB
	private ParametroServicio parametroServicio;

	@PostConstruct
	public void init() {
	}

	public void restaurarClave() {
		Usuario usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getNombreUsuario());
		if (usuario != null) {
			if (usuario.getCorreoElectronico().equals(getCorreoElectronico())) {

				String clave = GeneradorCodigo.generarCodigo(12);
				usuario.setContrasena(EncriptarCadenas.encriptarCadenaSha1(clave));

				usuario.setFechaModificacion(new Date());
				System.out.println("Enviar Correo");
				try {
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

					to.add(usuario.getCorreoElectronico());
					asunto.append("Notificación SANYR");

					mailMessage = credencialesCorreo();
					mailMessage.setTo(to);
					mailMessage.setSubject(asunto.toString());

					mailMessage.setText(html.toString());
					//mailServicio.sendMail(mailMessage);
					clienteQueueMailServicio.encolarMail(mailMessage);
					/*
					Email email = new Email();
					email.sendMail(usuario.getCorreoElectronico(), "Prueba", clave);

*/
					usuarioServicio.update(usuario);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Recuperación de Contraseña", getBundleMensaje("mensaje.restauracionClave", null)));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
		}
	}
	
	private MailMessage credencialesCorreo() {
		MailMessage credenciales = new MailMessage();
		credenciales.setFrom(parametroServicio.findByPk(ParametroEnum.MAIL_SANYR.name()).getValor());
		credenciales.setUsername(parametroServicio.findByPk(ParametroEnum.MAIL_USERNAME_SANYR.name()).getValor());
		credenciales.setPassword(parametroServicio.findByPk(ParametroEnum.MAIL_CONTRASENA_SANYR.name()).getValor());
		System.out.println(credenciales.getUsername());
		/*
		credenciales.setFrom("jadira.paspuel@dinardap.gob.ec");
		credenciales.setUsername("dinardap.capacitadora");
		System.out.println(credenciales.getUsername());
		credenciales.setPassword("aV-Capacitacion-3007");
		*/
		return credenciales;
	}
	

	// Getters & Setters
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
