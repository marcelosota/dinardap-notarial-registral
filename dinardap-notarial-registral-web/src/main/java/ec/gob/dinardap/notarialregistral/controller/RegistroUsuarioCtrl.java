package ec.gob.dinardap.notarialregistral.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.geografico.modelo.Canton;
import ec.gob.dinardap.geografico.modelo.Provincia;
import ec.gob.dinardap.geografico.servicio.CantonServicio;
import ec.gob.dinardap.geografico.servicio.ProvinciaServicio;
import ec.gob.dinardap.interoperadorv2.cliente.servicio.ServicioDINARDAP;
import ec.gob.dinardap.interoperadorv2.ws.ConsultarResponse;
import ec.gob.dinardap.notarialregistral.constante.InteroperabilidadEnum;
import ec.gob.dinardap.seguridad.dto.UsuarioDto;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.TipoInstitucion;
import ec.gob.dinardap.seguridad.servicio.AsignacionInstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.PerfilServicio;
import ec.gob.dinardap.seguridad.servicio.TipoInstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;


@Named(value="registroUsuarioCtrl")
@ViewScoped
public class RegistroUsuarioCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6455322718943194443L;
	
	@EJB
	private PerfilServicio perfilServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private InstitucionServicio  institucionServicio;
	
	@EJB
	private TipoInstitucionServicio tipoInstitucionServicio;
	
	@EJB
	private AsignacionInstitucionServicio asignacionInstitucionServicio;
	
	@EJB
	private ProvinciaServicio provinciaServicio;
	
	@EJB
	private CantonServicio cantonServicio;
	
	private UsuarioDto usuarioDto;
	private List<TipoInstitucion> listaTipoInstitucion;
	private List<Institucion> listaInstituciones;
	private List<Provincia> provincia;
	private List<Canton> canton; 
	private Integer tipoIdentificacion;
	private boolean asignarEntidad;
	private boolean flagCanton;
	private boolean flagTipoEntidad;
	private boolean flagEntidad;
	private boolean flagNombre;
	
	@PostConstruct
	protected void init() {
		limpiarCampos();
	}
	public void seleccionarTipoIdentificacion() {
		if(getTipoIdentificacion() == 1)
			setFlagNombre(true);
		else
			setFlagNombre(false);
	}
	
	public void buscarUsuario() {
		if(getTipoIdentificacion() == 1) {
			if(usuarioServicio.obtenerUsuarioPorIdentificacion(getUsuarioDto().getCedula()) == null) {
				ServicioDINARDAP ob = new ServicioDINARDAP();
				ConsultarResponse objWs;
				objWs = ob.obtenerDatosFuente(InteroperabilidadEnum.RC_PARAM.getPaquete(), 
						getUsuarioDto().getCedula(), 
						InteroperabilidadEnum.RC.getPaquete(), 
						InteroperabilidadEnum.RC_USUARIO.getPaquete(), 
						InteroperabilidadEnum.RC_CONTRASENA.getPaquete());
	
				if (objWs != null) {
					getUsuarioDto().setNombre( objWs.getPaquete().getEntidades().getEntidad().get(0).getFilas().getFila().get(0)
							.getColumnas().getColumna().get(3).getValor());
				}
				
			}else {
				addErrorMessage(null, getBundleMensaje("usuario.existe", null), null);
			}
		}
	}
	
	public void guardarUsuario() {
		getUsuarioDto().setContrasena(EncriptarCadenas.encriptarCadenaSha1(getUsuarioDto().getContrasena()));
		usuarioDto = usuarioServicio.crearUsuario(getUsuarioDto());
		if(isAsignarEntidad() && usuarioDto.getUsuarioId() != null) {
			asignacionInstitucionServicio.asignarUsuarioInstitucion(usuarioDto.getUsuarioId(), usuarioDto.getInstitucionId());
		}
		if(usuarioDto.getUsuarioId() != null) {	
			addInfoMessage(getBundleMensaje("registro.guardado", null), null);
			limpiarCampos();
		}else
			addInfoMessage(getBundleMensaje("revisar.informacion", null), null);
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
	
	public void limpiarCampos() {
		usuarioDto = new UsuarioDto();
		setAsignarEntidad(false);
		setFlagCanton(true);
		setFlagTipoEntidad(true);
		setFlagEntidad(true);
		setFlagNombre(true);
		setTipoIdentificacion(1);
	}
	
	public void limpiarInstitucion() {
		if(listaInstituciones != null) {
			listaInstituciones.clear();
			getUsuarioDto().setInstitucionId(null);
			setFlagEntidad(true);
		}
		
	}
	
	public UsuarioDto getUsuarioDto() {
		return usuarioDto;
	}

	public void setUsuarioDto(UsuarioDto usuarioDto) {
		this.usuarioDto = usuarioDto;
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

	public Integer getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(Integer tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public boolean isAsignarEntidad() {
		return asignarEntidad;
	}

	public void setAsignarEntidad(boolean asignarEntidad) {
		this.asignarEntidad = asignarEntidad;
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

	public boolean isFlagNombre() {
		return flagNombre;
	}

	public void setFlagNombre(boolean flagNombre) {
		this.flagNombre = flagNombre;
	}
	

}
