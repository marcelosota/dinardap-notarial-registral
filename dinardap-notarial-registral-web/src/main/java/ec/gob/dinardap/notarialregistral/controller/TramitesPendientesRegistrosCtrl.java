package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.compress.utils.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import ec.gob.dinardap.notarialregistral.constante.ContextoEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named(value = "tramitesPendientesRegistrosCtrl")
@ViewScoped
public class TramitesPendientesRegistrosCtrl extends BaseCtrl {
	private static final long serialVersionUID = 2441633867566660777L;
	@EJB
	TramiteDao tramiteDao;
	@EJB
	TramiteServicio tramiteServicio;
	@EJB
	DocumentoServicio documentoServicio;
	@EJB
	private ParametroServicio parametroServicio;

	private List<TramiteRegistradorDto> listaTramitePendiente;
	private List<TramiteRegistradorDto> filtro;
	private TramiteRegistradorDto selectedTramite;
	private TramiteRegistradorDto tramiteDto;
	private Long tramiteId;
	private Integer institucionId;
	private DocumentoDto documentoDto;
	private Boolean subirArchivo;

	@PostConstruct
	protected void init() {
		listaTramitePendiente = new ArrayList<>();
		tramiteDto = new TramiteRegistradorDto();
		selectedTramite = new TramiteRegistradorDto();
		tramiteDto.setTramite(new Tramite());
		tramiteId = 0L;
		documentoDto = new DocumentoDto();
		subirArchivo = true;

		//// modificar el canton del usuario logueado

	}

	public List<TramiteRegistradorDto> getListaTramitePendiente() {
		Integer cantonId = 178;
		listaTramitePendiente = tramiteDao.tramitesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), cantonId);
		if (filtro == null)
			filtro = listaTramitePendiente;
		return listaTramitePendiente;
	}

	public void setListaTramitePendiente(List<TramiteRegistradorDto> listaTramitePendiente) {
		this.listaTramitePendiente = listaTramitePendiente;
	}

	public List<TramiteRegistradorDto> getFiltro() {
		return filtro;
	}

	public void setFiltro(List<TramiteRegistradorDto> filtro) {
		this.filtro = filtro;
	}

	public TramiteRegistradorDto getSelectedTramite() {

		return selectedTramite;
	}

	public void setSelectedTramite(TramiteRegistradorDto selectedTramite) {
		this.selectedTramite = selectedTramite;
	}

	public TramiteRegistradorDto getTramiteDto() {
		tramiteDto.setTramite(tramiteServicio.findByPk(tramiteId));
		return tramiteDto;
	}

	public void setTramiteDto(TramiteRegistradorDto tramiteDto) {
		this.tramiteDto = tramiteDto;
	}

	public Long getTramiteId() {
		return tramiteId;
	}

	public void setTramiteId(Long tramiteId) {
		this.tramiteId = tramiteId;
	}

	public Integer getInstitucionId() {
		return institucionId;
	}

	public void setInstitucionId(Integer institucionId) {
		this.institucionId = institucionId;
	}

	public DocumentoDto getDocumentoDto() {
		return documentoDto;
	}

	public void setDocumentoDto(DocumentoDto documentoDto) {
		this.documentoDto = documentoDto;
	}

	public Boolean getSubirArchivo() {
		return subirArchivo;
	}

	public void setSubirArchivo(Boolean subirArchivo) {
		this.subirArchivo = subirArchivo;
	}

////////////////////funciones
	public void filaSeleccionada(SelectEvent event) {
		tramiteId = Long.parseLong(((TramiteRegistradorDto) event.getObject()).getTramiteId().toString());

	}

	public void descargarActoNotarial() {
		try {

			TipoArchivo tipoArchivo = new TipoArchivo();
			Documento documento = new Documento();
			documento = documentoServicio.buscarPorTramiteRegistros(tramiteDto.getTramite().getTramiteId(),
					ContextoEnum.NOTARIAL.getContexto());
			if (documento != null) {
				String ruta = documento.getRuta();
				int inicio = ruta.lastIndexOf("/");
				String nombre = ruta.substring(inicio + 1);
				System.out.println("nombre" + nombre);
				System.out.println("ruta" + ruta);
				byte[] archivoAdjunto = documentoServicio.descargarArchivo(ruta);
				downloadFile(archivoAdjunto, tipoArchivo.obtenerTipoArchivo(ruta), nombre);
			} else {

				addErrorMessage(null, getBundleMensaje("error.archivo", null), null);
			}

		} catch (Exception e) {
			addErrorMessage(null, getBundleMensaje("error.archivo", null), null);
			e.printStackTrace();
		}

	}

	public void subirArchivo(FileUploadEvent event) {
		try {
			Calendar c = Calendar.getInstance();
			/// calcula la fecha actual fechaInicio = c.getTime();
			String anio = String.valueOf(c.get(Calendar.YEAR));
			String mes = String.valueOf(c.get(Calendar.MONTH));
			String dia = String.valueOf(c.get(Calendar.DATE));
			String origen = null;
			System.out.println("tramiteobjeto"+tramiteDto.getTramite());

			if (tramiteDto.getTramite() != null) {

				origen = parametroServicio.findByPk(ParametroEnum.SFTP_NOTARIAL_REGISTRAL_RUTA.name()).getValor();
				String ruta =null;
						//origen.concat(anio).concat("/").concat(mes).concat("/").concat(dia).concat("/")
						//.concat(String.valueOf(tramiteDto.getTramite().getInstitucionId())).concat("/");
				System.out.println("subir" + ruta);

				Tramite objTramite = tramiteServicio.findByPk(tramiteId);
				documentoDto.setDocumento(new Documento());
				documentoDto.getDocumento().setTramite(objTramite);
				documentoDto.getDocumento().setRuta(ruta);
				documentoDto.getDocumento().setContextoArchivo(ContextoEnum.REGISTRAL.getContexto());
				documentoDto.getDocumento().setNombreCarga(event.getFile().getFileName());
				documentoDto.setContenido(IOUtils.toByteArray(event.getFile().getInputstream()));
				documentoDto.getDocumento().setEstado(EstadoEnum.ACTIVO.getEstado());			
				// cambiar al tener accesos
				// miArchivo.getDocumento().setSubidoPor(getLoggedUser());
				documentoDto.getDocumento().setSubidoPor(1);

				// setSubirArchivo(true);
			} else {
				Object[] param = new Object[1];
				param[0] = " documento registral";
				addErrorMessage(null, getBundleMensaje("error.subir.archivo", param), null);
			}

		} catch (IOException e) {
			Object[] param = new Object[1];
			param[0] = " documento registral";
			addErrorMessage(null, getBundleMensaje("error.subir.archivo", param), null);
			e.printStackTrace();
		}
	}
public void guardarTramite() {
	if(tramiteServicio.guardarRegistro(tramiteDto, documentoDto)==true)
	{
		addInfoMessage(getBundleMensaje("guardado.satisfactorio", null), null);
	}
	else
		addErrorMessage(null, getBundleMensaje("error.validacion", null), null);

	  
		/*
		if (beneficioDto.getInstitucion().getInstitucionId() > 0 && !beneficioDto.getCoberturaGeografica().equals("-1")
				&& beneficioDto.getTipoNaturaleza().getTipoNaturalezaId() > 0 && beneficioDto.getPeriodicidad()>0
				&& documentoHab.size() == beneficioDto.getArchivosDto().size()) {
			beneficioDto.setInstitucion(institucionServicio.findByPk(beneficioDto.getInstitucion().getInstitucionId()));
			beneficioDto.setTipoNaturaleza(
					tipoNatutalezaSevicio.findByPk(beneficioDto.getTipoNaturaleza().getTipoNaturalezaId()));
					*/
			//if (beneficioServicio.editarBeneficio(beneficioDto) == true) {
				///addInfoMessage(getBundleMensaje("guardado.satisfactorio", null), null);
			//} else
				//addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
			/*
		} else
			addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
			*/
	}

}
