package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.notarialregistral.dto.UsuarioDto;
import ec.gob.dinardap.seguridad.modelo.AsignacionInstitucion;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Perfil;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.PerfilServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named(value="editarUsuarioCtrl")
@ViewScoped
public class EditarUsuarioCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525661733927942070L;

	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private InstitucionServicio institucionServicio;
	
	@EJB
	private PerfilServicio perfilServicio;
	
	@EJB
	private AsignacionInstitucionServicio asignacionInstitucionServicio;
	
	@Inject
	private MisUsuariosCtrl misUsuariosCtrl;
	
	private Usuario usuario;
	private UsuarioDto usuarioDto;
	private List<Institucion> listaInstitucion;
	private List<SelectItem> estado;
	private String perfil;
	
	@PostConstruct
	public void init() {
		usuario = usuarioServicio.findByPk(misUsuariosCtrl.getUsuarioId());
		AsignacionInstitucion asignacionInstitucion = asignacionInstitucionServicio.findByPk(1);
		usuarioDto = new UsuarioDto();
		getUsuarioDto().setUsuarioId(getUsuario().getUsuarioId());
		getUsuarioDto().setCedula(getUsuario().getCedula());
		//getUsuarioDto().setContrasena(getUsuario().getContrasena());
		getUsuarioDto().setNombre(getUsuario().getNombre());
		getUsuarioDto().setInstitucionId(asignacionInstitucion.getInstitucion().getInstitucionId());
		//getUsuarioDto().setPerfilId(getUsuario().getPerfil().getPerfilId());
		getUsuarioDto().setEstado(getUsuario().getEstado());
		//setPerfil(getUsuario().getPerfil().getNombre());
	}
	
	/*public void buscarUsuario() {
		ServicioDINARDAP ob = new ServicioDINARDAP();
		ConsultarResponse objWs;
		objWs = ob.obtenerDatosInteroperabilidad(getUsuarioDto().getCedula(), "2639");
		if (objWs != null) {
			getUsuarioDto().setNombre( objWs.getPaquete().getEntidades().getEntidad().get(0).getFilas().getFila().get(0)
					.getColumnas().getColumna().get(3).getValor());
		}
	}*/
	
	public void guardarUsuario() {
		Institucion institucion = institucionServicio.findByPk(getUsuarioDto().getInstitucionId());
		Perfil perfil = new Perfil();
		/*if(institucion.getTipoInstitucion() == TipoEntidadEnum.DINARDAP.getTipo())
			perfil = perfilServicio.obtenerPorNombre(PerfilTurnoEnum.DINARDAP.getPerfil());
		else
			perfil = perfilServicio.obtenerPorNombre(PerfilTurnoEnum.RM.getPerfil());
		if(getUsuarioDto().getContrasena() != null && !getUsuarioDto().getContrasena().equals(""))
			getUsuarioDto().setContrasena(EncriptarCadenas.encriptarCadenaSha1(getUsuarioDto().getContrasena()));
		else
			getUsuarioDto().setContrasena(getUsuario().getContrasena());
		getUsuarioDto().setPerfilId(perfil.getPerfilId());
		usuarioServicio.modificarUsuario(getUsuarioDto());
		addInfoMessage(getBundleMensaje("registro.guardado", null), null);
		limpiarCampos();*/
	}
	
	public void limpiarCampos() {
		usuarioDto = new UsuarioDto();
		regresar();
	}
	
	public void regresar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("misUsuarios.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDto getUsuarioDto() {
		return usuarioDto;
	}

	public void setUsuarioDto(UsuarioDto usuarioDto) {
		this.usuarioDto = usuarioDto;
	}

	public List<Institucion> getListaInstitucion() {
		if(listaInstitucion == null) 
			listaInstitucion = institucionServicio.findAll();
		return listaInstitucion;
	}

	public void setListaRM(List<Institucion> listaRM) {
		this.listaInstitucion = listaRM;
	}

	public List<SelectItem> getEstado() {
		if(this.estado == null) {
			List<EstadoEnum> lista = new ArrayList<EstadoEnum>(EnumSet.allOf(EstadoEnum.class));
			this.estado = new ArrayList<SelectItem>();
			for(EstadoEnum valor : lista) {
				SelectItem item = new SelectItem(valor.getEstado(), valor.name());
				this.estado.add(item);
			}
		}
		return estado;
	}

	public void setEstado(List<SelectItem> estado) {
		this.estado = estado;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
}
