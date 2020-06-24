package ec.gob.dinardap.notarialregistral.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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

@Named(value = "misTramitesRegistrosCtrl")
@ViewScoped
public class MisTramitesRegistrosCtrl extends BaseCtrl {
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

	private List<TramiteRegistradorDto> listaTramitesCerrados;	
	private TramiteRegistradorDto selectedTramite;
	private TramiteRegistradorDto tramiteDto;
	private Long tramiteId;
	private Integer institucionId;
	private DocumentoDto documentoDto;
	private Short estadoTramite;
	private Usuario usuario;
	private String rutaNotarial;
	private String rutaRegistral;
	private Integer cantonId;

	@PostConstruct
	protected void init() {
		listaTramitesCerrados = new ArrayList<TramiteRegistradorDto>();
		tramiteDto = new TramiteRegistradorDto();
		selectedTramite = new TramiteRegistradorDto();
		tramiteDto.setTramite(new Tramite());
		documentoDto = new DocumentoDto();
		usuario = new Usuario();
		//usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getLoggedUser());
		usuario = usuarioServicio.findByPk(Integer.parseInt(getLoggedUser()));
		//// modificar el canton del usuario logueado
		usuario = usuarioServicio.obtenerUsuarioPorIdentificacion("1714284856");
		institucionId = Integer.parseInt(getSessionVariable("institucionId"));
		cantonId = institucionServicio.findByPk(institucionId).getCanton().getCantonId();

	}

	public List<TramiteRegistradorDto> getListaTramitesCerrados() {
		listaTramitesCerrados = tramiteDao.misTramites(ContextoEnum.NOTARIAL.getContexto(),
		ContextoEnum.REGISTRAL.getContexto(), EstadoTramiteEnum.CERRADO.getEstado(), cantonId);	
		
		return listaTramitesCerrados;
	}

	public void setListaTramitesCerrados(List<TramiteRegistradorDto> listaTramitesCerrados) {
		this.listaTramitesCerrados = listaTramitesCerrados;
	}	

	public TramiteRegistradorDto getSelectedTramite() {
		return selectedTramite;
	}

	public void setSelectedTramite(TramiteRegistradorDto selectedTramite) {
		this.selectedTramite = selectedTramite;
	}

	public TramiteRegistradorDto getTramiteDto() {
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

	public String getRutaNotarial() {
		return rutaNotarial;
	}

	public void setRutaNotarial(String rutaNotarial) {
		this.rutaNotarial = rutaNotarial;
	}

	public String getRutaRegistral() {
		return rutaRegistral;
	}

	public void setRutaRegistral(String rutaRegistral) {
		this.rutaRegistral = rutaRegistral;
	}

	public Integer getCantonId() {
		return cantonId;
	}

	public void setCantonId(Integer cantonId) {
		this.cantonId = cantonId;
	}

	////////// funciones
	public void descargarActoNotarial() {
		try {
			TipoArchivo tipoArchivo = new TipoArchivo();
			Documento documento = new Documento();
			documento = documentoServicio.buscarPorTramiteRegistros(getTramiteId(),
					ContextoEnum.NOTARIAL.getContexto());
			if (rutaNotarial != null) {
				String ruta = rutaNotarial;
				int inicio = ruta.lastIndexOf("/");
				String nombre = ruta.substring(inicio + 1);
				byte[] archivoAdjunto = documentoServicio.descargarArchivo(ruta);
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

	public void descargarDocumentoRegistral() {
		try {
			TipoArchivo tipoArchivo = new TipoArchivo();
			Documento documento = new Documento();
			documento = documentoServicio.buscarPorTramiteRegistros(getTramiteId(),
					ContextoEnum.REGISTRAL.getContexto());
			if (rutaRegistral != null) {
				String ruta = rutaRegistral;
				int inicio = ruta.lastIndexOf("/");
				String nombre = ruta.substring(inicio + 1);
				byte[] archivoAdjunto = documentoServicio.descargarArchivo(ruta);
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

	private void guardarLogDescarga(Documento documento) {
		FechaHoraSistema fecha = new FechaHoraSistema();
		try {
			LogDescarga logDescarga = new LogDescarga();
			logDescarga.setUsuarioId(usuario.getUsuarioId());
			logDescarga.setDocumento(documento);
			logDescarga.setFecha(fecha.convertirTimestamp(fecha.obtenerFechaHora()));
			// logDescarga.setResponsable(getLoggedUser());
			logDescarga.setResponsable("1714284856");
			if (logDescargaServicio.guardarLogDescarga(logDescarga) == true)
				System.out.println("guardadoLogDescarga");
			else
				System.out.println("no guardoLogDescarga");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
