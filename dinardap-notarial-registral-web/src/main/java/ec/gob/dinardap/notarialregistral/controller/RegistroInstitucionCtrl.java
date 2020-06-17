package ec.gob.dinardap.notarialregistral.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.geografico.modelo.Canton;
import ec.gob.dinardap.geografico.modelo.Provincia;
import ec.gob.dinardap.geografico.servicio.CantonServicio;
import ec.gob.dinardap.geografico.servicio.ProvinciaServicio;
import ec.gob.dinardap.interoperadorv2.cliente.servicio.ServicioDINARDAP;
import ec.gob.dinardap.interoperadorv2.ws.ConsultarResponse;
import ec.gob.dinardap.notarialregistral.constante.InteroperabilidadEnum;
import ec.gob.dinardap.seguridad.dto.InstitucionDto;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.TipoInstitucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.TipoInstitucionServicio;

@Named("registroInstitucionCtrl")
@ViewScoped
public class RegistroInstitucionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6394321610001332695L;

	@EJB
	private InstitucionServicio institucionServicio;
	
	@EJB
	private TipoInstitucionServicio tipoInstitucionServicio;
	
	@EJB
	private ProvinciaServicio provinciaServicio;
	
	@EJB
	private CantonServicio cantonServicio;
	
	private InstitucionDto institucionDto;
	private List<TipoInstitucion> listaTipoInstitucion;
	private List<Institucion> listaInstituciones;
	private List<Institucion> listaInstitucionesRectoras;
	private List<Provincia> provincia;
	private List<Canton> canton;
	private Integer tipoInstitucionRectora;
	private boolean flagCanton;
	private boolean flagAdscrita;
	private boolean flagRectora;
	private boolean flagGuardar;

	@PostConstruct
	protected void init() {
		limpiarCampos();
	}
	
	public void buscarInstitucion() {
		setFlagGuardar(true);
		if(institucionDto.getRuc() != null && institucionDto.getRuc().length() == 13) {
			if(institucionServicio.buscarInsttucionPorRuc(getInstitucionDto().getRuc()) == null){
				ServicioDINARDAP ob = new ServicioDINARDAP();
				ConsultarResponse objWs;
				objWs = ob.obtenerDatosFuente(InteroperabilidadEnum.SRI_PARAM.getPaquete(), 
						getInstitucionDto().getRuc(), 
						InteroperabilidadEnum.SRI.getPaquete(), 
						InteroperabilidadEnum.SRI_USUARIO.getPaquete(), 
						InteroperabilidadEnum.SRI_CONTRASENA.getPaquete());
	
				if (objWs != null) {
					getInstitucionDto().setNombre(objWs.getPaquete().getEntidades().getEntidad().get(0).getFilas().getFila().get(0)
							.getColumnas().getColumna().get(0).getValor());
					setFlagGuardar(false);
				}else
					addErrorMessage(null, getBundleMensaje("ruc.incorrecto", null), null);
			}else
				addErrorMessage(null, getBundleMensaje("institucion.existe", null), null);
		}else
			addErrorMessage(null, getBundleMensaje("ruc.longitud", null), null);
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
	
	public void limpiarCampos() {
		institucionDto = new InstitucionDto();
		setFlagCanton(true);
		setFlagAdscrita(false);
		setFlagRectora(true);
		setFlagGuardar(true);
	}
	
	public void guardarInstitucion() {
		if(institucionServicio.guardarInstitucion(getInstitucionDto())) {
			limpiarCampos();
			addInfoMessage(getBundleMensaje("registro.guardado", null), null);
		}else
			addInfoMessage(getBundleMensaje("revisar.informacion", null), null);
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

	public boolean isFlagGuardar() {
		return flagGuardar;
	}

	public void setFlagGuardar(boolean flagGuardar) {
		this.flagGuardar = flagGuardar;
	}

}
