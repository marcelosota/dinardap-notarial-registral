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

import ec.gob.dinardap.geografico.modelo.Canton;
import ec.gob.dinardap.geografico.modelo.Provincia;
import ec.gob.dinardap.geografico.servicio.CantonServicio;
import ec.gob.dinardap.geografico.servicio.ProvinciaServicio;
import ec.gob.dinardap.seguridad.dto.InstitucionDto;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.TipoInstitucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.TipoInstitucionServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named("editarInstitucionCtrl")
@ViewScoped
public class EditarInstitucionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3360289753177659744L;

	@EJB
	private InstitucionServicio institucionServicio;
	
	@EJB
	private ProvinciaServicio provinciaServicio;
	
	@EJB
	private CantonServicio cantonServicio;
	
	@EJB
	private TipoInstitucionServicio tipoInstitucionServicio;
	
	@Inject
	private MisInstitucionesCtrl misInstitucionesCtrl;
	
	private InstitucionDto institucionDto;
	private List<TipoInstitucion> listaTipoInstitucion;
	private List<Institucion> listaInstituciones;
	private List<Institucion> listaInstitucionesRectoras;
	private List<Provincia> provincia;
	private List<Canton> canton;
	private List<SelectItem> estado;
	private Integer tipoInstitucionRectora;
	private boolean flagCanton;
	private boolean flagAdscrita;
	private boolean flagRectora;

	@PostConstruct
	public void init() {
		Institucion institucion = institucionServicio.findByPk(misInstitucionesCtrl.getInstitucionId());
		institucionDto = new InstitucionDto();
		institucionDto.setInstitucionId(institucion.getInstitucionId());
		institucionDto.setRuc(institucion.getRuc());
		institucionDto.setNombre(institucion.getNombre());
		institucionDto.setSiglas(institucion.getSiglas());
		institucionDto.setTipoInstitucionId(institucion.getTipoInstitucion().getTipoInstitucionId());
		institucionDto.setProvinciaId(institucion.getCanton().getProvincia().getProvinciaId());
		institucionDto.setCantonId(institucion.getCanton().getCantonId());
		institucionDto.setCodigoIs(institucion.getCodigoIs());
		institucionDto.setFechaRegistro(institucion.getFechaRegistro());
		institucionDto.setEstado(institucion.getEstado());
		cantonPorProvincia();
		if(institucion.getInstitucion() != null) {
			institucionDto.setAdscrita(institucion.getInstitucion().getInstitucionId());
			setTipoInstitucionRectora(institucion.getInstitucion().getTipoInstitucion().getTipoInstitucionId());
			buscarInstitucionPorTipo();
			setFlagAdscrita(true);
		}else
			setFlagAdscrita(false);
	}
	
	public void guardarInstitucion() {
		if(!isFlagAdscrita()) 
			getInstitucionDto().setAdscrita(null);
		institucionServicio.actualizarInstitucion(getInstitucionDto());
		addInfoMessage(null, getBundleMensaje("usuario.existe", null));
		regresar();
	}
	
	public void regresar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("misInstituciones.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cantonPorProvincia() {
		if(canton != null && canton.size() > 0)
			canton.clear();
		if(getInstitucionDto().getProvinciaId() != null) {
			setCanton(cantonServicio.buscarCantonesPorProvincia(getInstitucionDto().getProvinciaId()));
			setFlagCanton(false);
		}else {
			setFlagCanton(true);
		}
			
	}
	
	public void buscarInstitucionPorTipo() {
		if(getTipoInstitucionRectora() != null && getTipoInstitucionRectora() > 0) {
			setFlagRectora(false);
			setListaInstitucionesRectoras(institucionServicio.buscarInstitucionPorTipo(getTipoInstitucionRectora())); 
		}
	}

	public InstitucionDto getInstitucionDto() {
		return institucionDto;
	}

	public void setInstitucionDto(InstitucionDto institucionDto) {
		this.institucionDto = institucionDto;
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

	public List<Institucion> getListaInstitucionesRectoras() {
		return listaInstitucionesRectoras;
	}

	public void setListaInstitucionesRectoras(List<Institucion> listaInstitucionesRectoras) {
		this.listaInstitucionesRectoras = listaInstitucionesRectoras;
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

	public Integer getTipoInstitucionRectora() {
		return tipoInstitucionRectora;
	}

	public void setTipoInstitucionRectora(Integer tipoInstitucionRectora) {
		this.tipoInstitucionRectora = tipoInstitucionRectora;
	}

	public boolean isFlagCanton() {
		return flagCanton;
	}

	public void setFlagCanton(boolean flagCanton) {
		this.flagCanton = flagCanton;
	}

	public boolean isFlagAdscrita() {
		return flagAdscrita;
	}

	public void setFlagAdscrita(boolean flagAdscrita) {
		this.flagAdscrita = flagAdscrita;
	}

	public boolean isFlagRectora() {
		return flagRectora;
	}

	public void setFlagRectora(boolean flagRectora) {
		this.flagRectora = flagRectora;
	}
}
