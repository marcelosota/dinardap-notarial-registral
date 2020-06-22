package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;


@Named(value="cambiarContrasenaCtrl")
@ViewScoped
public class CambiarContrasenaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5963176473739246926L;
	
	@EJB
	private UsuarioServicio usuarioServicio;

	private String cedula;
	private String contrasena;
	private String nuevaContrasena;
	private String repetirContrasena;
	
	public void modificarContrasena() {
		Usuario usuario = usuarioServicio.verificarCredenciales(usuarioServicio.findByPk(Integer.parseInt(getLoggedUser())).getCedula(), 
				EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getContrasena())));
		
		if(usuario != null) {
			usuarioServicio.actualizarContrasena(usuario.getCedula(), 
					EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getNuevaContrasena())));
			limpiarCampos();
			addInfoMessage(getBundleMensaje("cambiar.contrasena", null), null);
		}else
			addErrorMessage(null, getBundleMensaje("error.credenciales", null), null);
	}
	
	public void limpiarCampos() {
		setCedula("");
		setContrasena("");
		setNuevaContrasena("");
		setRepetirContrasena("");
	}
	
	public void regresar() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			//limpiarCampos();
			String valor = context.getRequestContextPath();
			context.redirect(valor + "/paginas/brand.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UsuarioServicio getUsuarioServicio() {
		return usuarioServicio;
	}
	public void setUsuarioServicio(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getNuevaContrasena() {
		return nuevaContrasena;
	}
	public void setNuevaContrasena(String nuevaContrasena) {
		this.nuevaContrasena = nuevaContrasena;
	}
	public String getRepetirContrasena() {
		return repetirContrasena;
	}
	public void setRepetirContrasena(String repetirContrasena) {
		this.repetirContrasena = repetirContrasena;
	}
	
}
