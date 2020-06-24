package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.compress.utils.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import ec.gob.dinardap.notarialregistral.constante.ContextoEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.servicio.LogDescargaServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.notarialregistral.util.FechaHoraSistema;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
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
	UsuarioServicio usuarioServicio;
	@EJB
	LogDescargaServicio logDescargaServicio;
    @EJB
    InstitucionServicio institucionServicio;
	
	@EJB
	private ParametroServicio parametroServicio;

	private List<TramiteRegistradorDto> listaTramitePendiente;
	private TramiteRegistradorDto selectedTramite;
	private TramiteRegistradorDto tramiteDto;
	private Long tramiteId;
	private Integer institucionId;
	private DocumentoDto documentoDto;
	private Boolean subirArchivoB;
	private Short estadoTramite;
	private Boolean estadoInconsistente;
	private Usuario usuario;
	
	

	@PostConstruct
	protected void init() {
		listaTramitePendiente = new ArrayList<>();
		tramiteDto = new TramiteRegistradorDto();
		selectedTramite = new TramiteRegistradorDto();
		tramiteDto.setTramite(new Tramite());
		tramiteId = 0L;
		documentoDto = new DocumentoDto();
		subirArchivoB = true;
		estadoInconsistente = true;
		usuario = new Usuario();
		//usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getLoggedUser());
		usuario = usuarioServicio.findByPk(Integer.parseInt(getLoggedUser()));
		institucionId = Integer.parseInt(getSessionVariable("institucionId"));
		//// modificar el canton del usuario logueado

	}

	public List<TramiteRegistradorDto> getListaTramitePendiente() {		
		Integer cantonId = institucionServicio.findByPk(institucionId).getCanton().getCantonId();	
				
		listaTramitePendiente = tramiteDao.tramitesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), cantonId);		
		
		return listaTramitePendiente;
	}

	public void setListaTramitePendiente(List<TramiteRegistradorDto> listaTramitePendiente) {
		this.listaTramitePendiente = listaTramitePendiente;
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

	public Short getEstadoTramite() {
		return estadoTramite;
	}

	public void setEstadoTramite(Short estadoTramite) {
		this.estadoTramite = estadoTramite;
	}

	public Boolean getSubirArchivoB() {
		return subirArchivoB;
	}

	public void setSubirArchivoB(Boolean subirArchivoB) {
		this.subirArchivoB = subirArchivoB;
	}

	public Boolean getEstadoInconsistente() {
		return estadoInconsistente;
	}

	public void setEstadoInconsistente(Boolean estadoInconsistente) {
		this.estadoInconsistente = estadoInconsistente;
	}

	//////////////////// funciones
	public void filaSeleccionada(SelectEvent event) {
		tramiteId = Long.parseLong(((TramiteRegistradorDto) event.getObject()).getTramiteId().toString());

	}

	public void descargarActoNotarial() {
		try {
			FechaHoraSistema fecha = new FechaHoraSistema();
			TipoArchivo tipoArchivo = new TipoArchivo();
			Documento documento = new Documento();
			documento = documentoServicio.buscarPorTramiteRegistros(tramiteDto.getTramite().getTramiteId(),
					ContextoEnum.NOTARIAL.getContexto());
			if (documento != null) {
				String ruta = documento.getRuta();
				int inicio = ruta.lastIndexOf("/");
				String nombre = ruta.substring(inicio + 1);
				byte[] archivoAdjunto = documentoServicio.descargarArchivo(ruta);
				tramiteDto.setFechaDescarga(fecha.convertirTimestamp(fecha.obtenerFechaHora()));
				guardarLogDescarga(documento);
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
			int mesActual = (c.get(Calendar.MONTH)) + 1;
			System.out.println("mes" + mesActual);
			String anio = String.valueOf(c.get(Calendar.YEAR));
			String mes = String.valueOf(mesActual);
			String dia = String.valueOf(c.get(Calendar.DATE));
			//String origen = null;
			System.out.println("tramiteobjeto" + tramiteDto.getTramite());

			if (tramiteDto.getTramite() != null) {

				//origen = parametroServicio.findByPk(ParametroEnum.SFTP_NOTARIAL_REGISTRAL_RUTA.name()).getValor();
				String ruta = anio.concat("/").concat(mes).concat("/").concat(dia).concat("/")
						.concat(getInstitucionId().toString()).concat("/");
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
				documentoDto.getDocumento().setSubidoPor(usuario.getUsuarioId());

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

	public void cambiarEstadoTramite() {
		if (estadoTramite == EstadoTramiteEnum.INCONSISTENTE.getEstado())
			estadoInconsistente = false;
		else
			estadoInconsistente = true;

	}

	private void guardarLogDescarga(Documento documento) {
		try {
			LogDescarga logDescarga = new LogDescarga();
			logDescarga.setUsuarioId(usuario.getUsuarioId());
			logDescarga.setDocumento(documento);
			logDescarga.setFecha(tramiteDto.getFechaDescarga());
			// logDescarga.setResponsable(getLoggedUser());
			logDescarga.setResponsable(usuario.getCedula());
			if (logDescargaServicio.guardarLogDescarga(logDescarga) == true)
				System.out.println("guardadoLogDescarga");
			else
				System.out.println("no guardoLogDescarga");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	public void limpiar()
	{
		
		listaTramitePendiente = new ArrayList<>();
		tramiteDto = new TramiteRegistradorDto();
		selectedTramite = new TramiteRegistradorDto();
		tramiteDto.setTramite(new Tramite());
		tramiteId = 0L;
		documentoDto = new DocumentoDto();		
		estadoInconsistente = true;
	}

	public void guardarTramite() {
		try {

			if (tramiteId != 0L) {
				// si el estado es inconsistente no debe cargar el documento y si observar
				if (estadoTramite == EstadoTramiteEnum.INCONSISTENTE.getEstado()) {
					// tramiteDto.setCerradoPor(getLoggedUser());
					tramiteDto.setCerradoPor(usuario);
					tramiteDto.setEstado(EstadoTramiteEnum.INCONSISTENTE.getEstado());
					if (tramiteServicio.guardarRegistro(tramiteDto) == true)
					{
						 limpiar();
						addInfoMessage(getBundleMensaje("registro.guardado", null), null);
					}
					else
						addErrorMessage(null, getBundleMensaje("error.validacion", null), null);

				}
				if (estadoTramite == EstadoTramiteEnum.CERRADO.getEstado()) {
					// tramiteDto.setCerradoPor(getLoggedUser());
					tramiteDto.setCerradoPor(usuario);
					tramiteDto.setEstado(EstadoTramiteEnum.CERRADO.getEstado());

					if (documentoDto.getContenido() != null) {
						if (documentoServicio.subirArchivos(documentoDto) == true) {
							System.out.println("documento registral subido");
							if (tramiteServicio.guardarRegistro(tramiteDto) == true)
							{
								limpiar();
								addInfoMessage(getBundleMensaje("registro.guardado", null), null);
							}	
							else
								addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
						} else {
							Object[] param = new Object[1];
							param[0] = " documento registral";
							addErrorMessage(null, getBundleMensaje("error.subir.archivo", param), null);
						}

					} else
						addErrorMessage(null, "Documento registral" + getBundleMensaje("requerido", null), null);

				}
			} else
				addErrorMessage(null, "No ha seleccionado el trámite", null);

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, getBundleMensaje("error.validacion", null), null);

		}

	}

}
