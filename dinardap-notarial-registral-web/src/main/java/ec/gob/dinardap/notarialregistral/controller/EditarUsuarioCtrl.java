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

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.geografico.modelo.Canton;
import ec.gob.dinardap.geografico.modelo.Provincia;
import ec.gob.dinardap.geografico.servicio.CantonServicio;
import ec.gob.dinardap.geografico.servicio.ProvinciaServicio;
import ec.gob.dinardap.seguridad.dto.UsuarioDto;
import ec.gob.dinardap.seguridad.dto.UsuarioInstitucionDto;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.TipoInstitucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.PerfilServicio;
import ec.gob.dinardap.seguridad.servicio.TipoInstitucionServicio;
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
	private TipoInstitucionServicio tipoInstitucionServicio;
	
	@EJB
	private PerfilServicio perfilServicio;
	
	@EJB
	private AsignacionInstitucionServicio asignacionInstitucionServicio;
	
	@EJB
	private ProvinciaServicio provinciaServicio;
	
	@EJB
	private CantonServicio cantonServicio;
	
	@Inject
	private MisUsuariosCtrl misUsuariosCtrl;
	
	private Usuario usuario;
	private UsuarioDto usuarioDto;
	private List<SelectItem> estado;
	private List<UsuarioInstitucionDto> listaAsignaciones;
	private List<TipoInstitucion> listaTipoInstitucion;
	private List<Institucion> listaInstituciones;
	private List<Provincia> provincia;
	private List<Canton> canton; 
	private String perfil;
	private Integer asignacionInstitucionId;
	private UsuarioInstitucionDto asgSeleccionada;
	private boolean estadoAsg;
	private boolean flagCanton;
	private boolean flagTipoEntidad;
	private boolean flagEntidad;
	private boolean asignarEntidad;
	
	@PostConstruct
	public void init() {
		usuario = usuarioServicio.findByPk(misUsuariosCtrl.getUsuarioId());
		usuarioDto = new UsuarioDto();
		listaAsignaciones = new ArrayList<UsuarioInstitucionDto>();
		getUsuarioDto().setUsuarioId(getUsuario().getUsuarioId());
		getUsuarioDto().setCedula(getUsuario().getCedula());
		getUsuarioDto().setNombre(getUsuario().getNombre());
		getUsuarioDto().setCargo(getUsuario().getCargo());
		getUsuarioDto().setCorreoElectronico(getUsuario().getCorreoElectronico());
		getUsuarioDto().setTelefono(getUsuario().getTelefono());
		getUsuarioDto().setFechaCreacion(getUsuario().getFechaCreacion());
		getUsuarioDto().setEstado(getUsuario().getEstado());
		listaAsignaciones = asignacionInstitucionServicio.buscarAsignacionesPorUsuario(misUsuariosCtrl.getUsuarioId());
	}
	
	public void guardarUsuario() {
		if(isAsignarEntidad() && verificarInstitucinesAsignadas()) {
			addErrorMessage(null, getBundleMensaje("usuario.institucion.existente", null), null);
		}else {
			if(getUsuarioDto().getContrasena() != null && !getUsuarioDto().getContrasena().trim().equals(""))
				getUsuarioDto().setContrasena(EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getUsuarioDto().getContrasena())));
			else
				getUsuarioDto().setContrasena(getUsuario().getContrasena());
			usuarioServicio.modificarUsuario(getUsuarioDto());
			addInfoMessage(getBundleMensaje("registro.guardado", null), null);
			limpiarCampos();
			regresar();
		}
		
	}
	
	public void limpiarCampos() {
		usuarioDto = new UsuarioDto();
		setAsignarEntidad(false);
		setFlagCanton(true);
		setFlagTipoEntidad(true);
		setFlagEntidad(true);
	}
	
	public void regresar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("misUsuarios.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verificarAsignaciones() {
		boolean flag = true;
		if(getAsgSeleccionada().getEstado()) {
			if(verificarInstitucinesAsignadas()) {
				addErrorMessage(null, getBundleMensaje("usuario.institucion.existente", null), null);
				flag = false;
			}
		}	
		if(flag){
			asignacionInstitucionServicio.actualizarAsignacion(asgSeleccionada);
			listaAsignaciones = asignacionInstitucionServicio.buscarAsignacionesPorUsuario(misUsuariosCtrl.getUsuarioId());
			addInfoMessage(getBundleMensaje("registro.guardado", null), null);
		}

	}
	
	public void habilitarAsignacion() {
		if(isAsignarEntidad()) {
			if(verificarInstitucinesAsignadas()) {
				addErrorMessage(null, getBundleMensaje("usuario.institucion.existente", null), null);
				setAsignarEntidad(false);
				getUsuarioDto().setInstitucionId(null);
			}
		}	
	}
	
	private boolean verificarInstitucinesAsignadas() {
		return asignacionInstitucionServicio.verificarAsignacionPorEstado(getUsuarioDto().getUsuarioId(), EstadoEnum.ACTIVO.getEstado());
	}
	
	public void cantonPorProvincia() {
		if(canton != null && canton.size() > 0)
			canton.clear();
		if(getUsuarioDto().getProvinciaId() != null) {
			setCanton(cantonServicio.buscarCantonesPorProvincia(getUsuarioDto().getProvinciaId()));
			setFlagCanton(false);
			limpiarInstitucion();
		}else {
			setFlagCanton(true);
			setFlagTipoEntidad(true);
			setFlagEntidad(true);
		}
			
	}
	
	public void cantonSeleccionado() {
		if(getUsuarioDto().getCantonId() != null && getUsuarioDto().getCantonId() > 0) {
			setFlagTipoEntidad(false);
		}
	}
	
	public void buscarInstitucion() {
		if(getUsuarioDto().getTipoInstitucionId() != null && getUsuarioDto().getTipoInstitucionId() > 0) {
			limpiarInstitucion();
			setFlagEntidad(false);
			setListaInstituciones(institucionServicio.
				obtenerInstitucionesTipoCantonEstado(getUsuarioDto().getTipoInstitucionId(), 
						getUsuarioDto().getCantonId(), 
						EstadoEnum.ACTIVO.getEstado()));
		}
	}
	
	public void limpiarInstitucion() {
		if(listaInstituciones != null) {
			listaInstituciones.clear();
			getUsuarioDto().setInstitucionId(null);
			setFlagEntidad(true);
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

	public List<UsuarioInstitucionDto> getListaAsignaciones() {
		return listaAsignaciones;
	}

	public void setListaAsignaciones(List<UsuarioInstitucionDto> listaAsignaciones) {
		this.listaAsignaciones = listaAsignaciones;
	}

	public List<TipoInstitucion> getListaTipoInstitucion() {
		if(listaTipoInstitucion == null || listaTipoInstitucion.isEmpty())
			listaTipoInstitucion = tipoInstitucionServicio.tipoInstitucionActivas();
		return listaTipoInstitucion;
	}

	public void setListaTipoInstitucion(List<TipoInstitucion> listaTipoInstitucion) {
		this.listaTipoInstitucion = listaTipoInstitucion;
	}

	public List<Institucion> getListaInstituciones() {
		return listaInstituciones;
	}
	public void setListaInstituciones(List<Institucion> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
	}

	public List<Provincia> getProvincia() {
		provincia = new ArrayList<Provincia>();
		provincia = provinciaServicio.obtenerProvincias();
		return provincia;
	}

	public void setProvincia(List<Provincia> provincia) {
		this.provincia = provincia;
	}

	public List<Canton> getCanton() {
		return canton;
	}

	public void setCanton(List<Canton> canton) {
		this.canton = canton;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}


	public Integer getAsignacionInstitucionId() {
		return asignacionInstitucionId;
	}

	public void setAsignacionInstitucionId(Integer asignacionInstitucionId) {
		this.asignacionInstitucionId = asignacionInstitucionId;
	}

	public UsuarioInstitucionDto getAsgSeleccionada() {
		return asgSeleccionada;
	}

	public void setAsgSeleccionada(UsuarioInstitucionDto asgSeleccionada) {
		this.asgSeleccionada = asgSeleccionada;
	}

	public boolean isEstadoAsg() {
		return estadoAsg;
	}


	public void setEstadoAsg(boolean estadoAsg) {
		this.estadoAsg = estadoAsg;
	}

	public boolean isFlagCanton() {
		return flagCanton;
	}

	public void setFlagCanton(boolean flagCanton) {
		this.flagCanton = flagCanton;
	}

	public boolean isFlagTipoEntidad() {
		return flagTipoEntidad;
	}

	public void setFlagTipoEntidad(boolean flagTipoEntidad) {
		this.flagTipoEntidad = flagTipoEntidad;
	}

	public boolean isFlagEntidad() {
		return flagEntidad;
	}

	public void setFlagEntidad(boolean flagEntidad) {
		this.flagEntidad = flagEntidad;
	}

	public boolean isAsignarEntidad() {
		return asignarEntidad;
	}

	public void setAsignarEntidad(boolean asignarEntidad) {
		this.asignarEntidad = asignarEntidad;
	}
}
