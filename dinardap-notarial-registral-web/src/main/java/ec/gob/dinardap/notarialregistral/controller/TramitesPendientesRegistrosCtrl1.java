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
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
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

@Named(value = "tramitesPendientesRegistrosCtrl1")
@ViewScoped
public class TramitesPendientesRegistrosCtrl1 extends BaseCtrl {

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
    private List<TramiteRegistradorDto> filtro;
    private TramiteRegistradorDto selectedTramite;
    private TramiteRegistradorDto tramiteDto;
    private Integer institucionId;
    private DocumentoDto documentoDto;
    private Boolean subirArchivoB;
    private Short estadoTramite;
    private Boolean estadoInconsistente;
    private Usuario usuario;
    private String ctvu;

    @PostConstruct
    protected void init() {

        tramiteDto = new TramiteRegistradorDto();
        selectedTramite = new TramiteRegistradorDto();
        tramiteDto.setTramite(new Tramite());
        documentoDto = new DocumentoDto();
        subirArchivoB = true;
        estadoInconsistente = true;
        usuario = new Usuario();
        // usuario = usuarioServicio.obtenerUsuarioPorIdentificacion(getLoggedUser());
        usuario = usuarioServicio.findByPk(Integer.parseInt(getLoggedUser()));
        institucionId = Integer.parseInt(getSessionVariable("institucionId"));
        //// modificar el canton del usuario logueado		

    }

    public TramiteRegistradorDto getSelectedTramite() {

        return selectedTramite;
    }

    public void setSelectedTramite(TramiteRegistradorDto selectedTramite) {
        this.selectedTramite = selectedTramite;
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

    public String getCtvu() {
        return ctvu;
    }

    public void setCtvu(String ctvu) {
        this.ctvu = ctvu;
    }

    public TramiteRegistradorDto getTramiteDto() {
        return tramiteDto;
    }

    public void setTramiteDto(TramiteRegistradorDto tramiteDto) {
        this.tramiteDto = tramiteDto;
    }

    public List<TramiteRegistradorDto> getListaTramitePendiente() {

        listaTramitePendiente = tramiteDao.tramitesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), institucionId);
        if (filtro == null) {
            filtro = listaTramitePendiente;
        }

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

    //////////////////// funciones
    public void refrescarDtb() {
        listaTramitePendiente = tramiteDao.tramitesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), institucionId);
        if (filtro == null) {
            filtro = listaTramitePendiente;
        }
    }

    public void filaSeleccionada(SelectEvent event) {
        ctvu = selectedTramite.getCodigo();
        //ctvu = ((TramiteRegistradorDto) event.getObject()).getCodigo();
        consultarPorCvtu();
    }

    public void consultarPorCvtu() {
        tramiteDto.setTramite(new Tramite());
        try {
            if (getCtvu() != null) {

                tramiteDto.setTramite(tramiteServicio.tramiteCargadosByCodigoValidacionTramite(ctvu));

                if (tramiteDto.getTramite() == null) {
                    String mensaje = getBundleMensaje("error.codigo.registro", null);
                    addErrorMessage(null, mensaje, null);
                }

            } else {

                String mensaje = getBundleMensaje("requerido", null);
                addErrorMessage(null, "Código de Validación de Trámite Único" + mensaje, null);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            String mensaje = getBundleMensaje("error.codigo.registro", null);
            addErrorMessage(null, mensaje, null);

        }

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
            String origen = null;
            System.out.println("tramiteobjeto" + tramiteDto.getTramite());

            if (tramiteDto.getTramite() != null) {

                origen = parametroServicio.findByPk(ParametroEnum.SFTP_NOTARIAL_REGISTRAL_RUTA.name()).getValor();
                String ruta = origen.concat(anio).concat("/").concat(mes).concat("/").concat(dia).concat("/")
                        .concat(getInstitucionId().toString()).concat("/");
                System.out.println("subir" + ruta);

                Tramite objTramite = tramiteServicio.findByPk(tramiteDto.getTramite().getTramiteId());
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
        if (estadoTramite == EstadoTramiteEnum.INCONSISTENTE.getEstado()) {
            estadoInconsistente = false;
        } else {
            estadoInconsistente = true;
        }

    }

    private void guardarLogDescarga(Documento documento) {
        try {
            LogDescarga logDescarga = new LogDescarga();
            logDescarga.setUsuarioId(usuario.getUsuarioId());
            logDescarga.setDocumento(documento);
            logDescarga.setFecha(tramiteDto.getFechaDescarga());
            // logDescarga.setResponsable(getLoggedUser());
            logDescarga.setResponsable(usuario.getCedula());
            if (logDescargaServicio.guardarLogDescarga(logDescarga) == true) {
                System.out.println("guardadoLogDescarga");
            } else {
                System.out.println("no guardoLogDescarga");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void limpiar() {

        tramiteDto = new TramiteRegistradorDto();
        selectedTramite = new TramiteRegistradorDto();
        tramiteDto.setTramite(new Tramite());
        documentoDto = new DocumentoDto();
        documentoDto.setDocumento(new Documento());
        estadoInconsistente = true;
        setCtvu(null);
        setEstadoTramite(null);
        listaTramitePendiente = new ArrayList<TramiteRegistradorDto>();

    }

    public Boolean verificarEstadoTramite() {
        Tramite tramite = new Tramite();
        tramite = tramiteServicio.tramiteCargadosByCodigoValidacionTramite(ctvu);
        if (tramite != null) {
            return true;
        } else {
            return false;
        }

    }

    public void guardarTramite() {
        try {

            if (getCtvu() != null) {
                // si el estado es inconsistente no debe cargar el documento y si observar
                if (estadoTramite == EstadoTramiteEnum.INCONSISTENTE.getEstado()) {
                    // tramiteDto.setCerradoPor(getLoggedUser());
                    if (verificarEstadoTramite() == true) {
                        tramiteDto.setCerradoPor(usuario);
                        tramiteDto.setEstado(EstadoTramiteEnum.INCONSISTENTE.getEstado());
                        if (tramiteServicio.guardarRegistro(tramiteDto) == true) {
                            String inconsistente = "tiene una inconsistencia que ha sido observada por el registrador.";
                            tramiteServicio.emailRegistros(tramiteDto, inconsistente);
                            limpiar();
                            addInfoMessage(getBundleMensaje("registro.guardado", null), null);
                        } else {
                            addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
                        }
                    } else {
                        addErrorMessage(null, getBundleMensaje("error.tramite.cerrado", null), null);
                    }

                }
                if (estadoTramite == EstadoTramiteEnum.CERRADO.getEstado()) {
                    // tramiteDto.setCerradoPor(getLoggedUser());
                    if (verificarEstadoTramite() == true) {
                        tramiteDto.setCerradoPor(usuario);
                        tramiteDto.setEstado(EstadoTramiteEnum.CERRADO.getEstado());

                        if (documentoDto.getContenido() != null) {
                            if (documentoServicio.subirArchivos(documentoDto) == true) {
                                System.out.println("documento registral subido");
                                if (tramiteServicio.guardarRegistro(tramiteDto) == true) {
                                    String atendido = "ha sido atendido y cerrado por el registrador.";
                                    tramiteServicio.emailRegistros(tramiteDto, atendido);
                                    limpiar();
                                    addInfoMessage(getBundleMensaje("registro.guardado", null), null);
                                } else {
                                    addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
                                }
                            } else {
                                Object[] param = new Object[1];
                                param[0] = " documento registral";
                                addErrorMessage(null, getBundleMensaje("error.subir.archivo", param), null);
                            }

                        } else {
                            addErrorMessage(null, "Documento registral" + getBundleMensaje("requerido", null), null);
                        }
                    } else {
                        addErrorMessage(null, getBundleMensaje("error.tramite.cerrado", null), null);
                    }

                }
            } else {
                String mensaje = getBundleMensaje("requerido", null);
                addErrorMessage(null, "Código de Validación de Trámite Único" + mensaje, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage(null, getBundleMensaje("error.validacion", null), null);

        }

    }

    public void cancelar() {
        limpiar();
    }

    public void cancelar1() {
        listaTramitePendiente = new ArrayList<TramiteRegistradorDto>();
    }

    public void print() {
        System.out.println("imprimir");
    }

    public void descargarRegistro() {
        TipoArchivo tipoArchivo = new TipoArchivo();

        if (documentoDto.getContenido() != null) {
            // descarga de memoria
            downloadFile(documentoDto.getContenido(),
                    tipoArchivo.obtenerTipoArchivo(documentoDto.getDocumento().getNombreCarga()),
                    documentoDto.getDocumento().getNombreCarga());

        } else {

            addErrorMessage(null, getBundleMensaje("error.archivo", null), null);
        }

    }

}
