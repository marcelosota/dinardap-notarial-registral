package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named("misInstitucionesCtrl")
@SessionScoped
public class MisInstitucionesCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 660563404532178496L;

	@EJB
	private InstitucionServicio institucionServicio;
	
	private List<Institucion> listaInstituciones;
	private List<Institucion> filtro;
	private Integer institucionId;
	
	public void verInstitucion() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("editarInstitucion.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refrescarDtb() {
		listaInstituciones = institucionServicio.findAll();
	}
	
	public String obtenerEstado(Short estado) {
		return EstadoEnum.obtenerEstadoPorCodigo(estado).name();
	}

	public List<Institucion> getListaInstituciones() {
		if(listaInstituciones == null)
			listaInstituciones = institucionServicio.findAll();
		return listaInstituciones;
	}
	
	public void setListaInstituciones(List<Institucion> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
	}

	public List<Institucion> getFiltro() {
		return filtro;
	}

	public void setFiltro(List<Institucion> filtro) {
		this.filtro = filtro;
	}

	public Integer getInstitucionId() {
		return institucionId;
	}

	public void setInstitucionId(Integer institucionId) {
		this.institucionId = institucionId;
	}

}
