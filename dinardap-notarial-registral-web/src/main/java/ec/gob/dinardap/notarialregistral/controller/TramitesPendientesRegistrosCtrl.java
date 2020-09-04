package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ec.gob.dinardap.notarialregistral.constante.ContextoEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.servicio.LogDescargaServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.notarialregistral.util.FechaHoraSistema;
import ec.gob.dinardap.seguridad.modelo.Usuario;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.TipoArchivo;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named(value = "tramitesPendientesRegistrosCtrl")
@ViewScoped
public class TramitesPendientesRegistrosCtrl extends BaseCtrl {

    private static final long serialVersionUID = 2441633867566660777L;

    //Declaración de variables
    //Variables de control visual
    private Boolean onTamiteSelect;
    private Boolean onInconsistente;
    private Boolean onCerrado;
    private Boolean disabledDescargar;
    private Boolean disabledFinalizar;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Usuario usuario;
    private Tramite tramiteSelected;
    private Short estadoTramite;
    private UploadedFile file;
    private byte[] fileByte;

    private DocumentoDto documentoDto;

    //Listas
    private List<Tramite> tramiteList;

    @EJB
    private TramiteDao tramiteDao;

    @EJB
    private DocumentoServicio documentoServicio;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private LogDescargaServicio logDescargaServicio;

    @EJB
    private TramiteServicio tramiteServicio;

    @EJB
    private ParametroServicio parametroServicio;

    @PostConstruct
    protected void init() {
        institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId"));
        usuarioId = Integer.parseInt(BaseCtrl.getSessionVariable("usuarioId"));
        usuario = usuarioServicio.findByPk(usuarioId);

        tramiteSelected = new Tramite();

        tramiteList = tramiteDao.getActosNotarialesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), institucionId);

        onTamiteSelect = Boolean.FALSE;
        onInconsistente = Boolean.FALSE;
        onCerrado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;
        disabledFinalizar = Boolean.TRUE;
    }

    public void onSelectTramite() {
        onTamiteSelect = Boolean.TRUE;
    }

    public void cambiarEstadoTramite() {
        if (estadoTramite.equals(EstadoTramiteEnum.INCONSISTENTE.getEstado())) {
            tramiteSelected.setEstado(EstadoTramiteEnum.INCONSISTENTE.getEstado());
            onInconsistente = Boolean.TRUE;
            onCerrado = Boolean.FALSE;
            disabledFinalizar = Boolean.FALSE;
        } else if (estadoTramite.equals(EstadoTramiteEnum.CERRADO.getEstado())) {
            tramiteSelected.setEstado(EstadoTramiteEnum.CERRADO.getEstado());
            onCerrado = Boolean.TRUE;
            onInconsistente = Boolean.FALSE;
            disabledFinalizar = Boolean.TRUE;
        }
    }

    public void cancelar() {
        tramiteSelected = new Tramite();

        estadoTramite = null;
        onTamiteSelect = Boolean.FALSE;
        onInconsistente = Boolean.FALSE;
        onCerrado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;
        disabledFinalizar = Boolean.TRUE;

    }

    public void visualizarArchivo() {
        if (fileByte != null) {
            downloadFile(fileByte, file.getContentType(), file.getFileName());
        }
    }

    private void subirArchivo() {
            Calendar c = Calendar.getInstance();
            int mesActual = (c.get(Calendar.MONTH)) + 1;
            String anio = String.valueOf(c.get(Calendar.YEAR));
            String mes = String.valueOf(mesActual);
            String dia = String.valueOf(c.get(Calendar.DATE));
            String origen = null;

            if (tramiteSelected != null) {
                origen = parametroServicio.findByPk(ParametroEnum.SFTP_NOTARIAL_REGISTRAL_RUTA.name()).getValor();
                String ruta = origen.concat(anio).concat("/").concat(mes).concat("/").concat(dia).concat("/")
                        .concat(institucionId.toString()).concat("/");

                Tramite objTramite = tramiteServicio.findByPk(tramiteSelected.getTramiteId());
                documentoDto = new DocumentoDto();
                documentoDto.setDocumento(new Documento());
                documentoDto.getDocumento().setTramite(objTramite);
                documentoDto.getDocumento().setRuta(ruta);
                documentoDto.getDocumento().setContextoArchivo(ContextoEnum.REGISTRAL.getContexto());
                documentoDto.getDocumento().setNombreCarga(file.getFileName());
                documentoDto.setContenido(fileByte);
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
    }

    public void finalizarTramite() {
        tramiteSelected.setCerradoPor(usuario);
        tramiteSelected.setFechaCierre(new Date());
        if (tramiteSelected.getEstado().equals(EstadoTramiteEnum.INCONSISTENTE.getEstado())) {
            tramiteServicio.update(tramiteSelected);
            String inconsistente = "tiene una Inconsistencia que ha sido observada por el Registro.";
            tramiteServicio.emailRegistrosCG(tramiteSelected, inconsistente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
                    getBundleMensaje("mensaje.tramiteFinalizadoRegistroInconsistente", null)));
        } else if (tramiteSelected.getEstado().equals(EstadoTramiteEnum.CERRADO.getEstado())) {
            subirArchivo();
            if (documentoServicio.subirArchivos(documentoDto)) {
                tramiteServicio.update(tramiteSelected);
                String atendido = "ha sido atendido y cerrado por el registrador.";
                tramiteServicio.emailRegistrosCG(tramiteSelected, atendido);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
                        getBundleMensaje("mensaje.tramiteFinalizadoRegistroCerrado", null)));
            } else {
                addErrorMessage(null, "Documento registral" + getBundleMensaje("requerido", null), null);
            }
        }

        tramiteList = new ArrayList<Tramite>();
        tramiteList = tramiteDao.getActosNotarialesPendientes(EstadoTramiteEnum.CARGADO.getEstado(), institucionId);
        onTamiteSelect = Boolean.FALSE;
        onInconsistente = Boolean.FALSE;
        onCerrado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;
        disabledFinalizar = Boolean.TRUE;
    }

    public void descargarActoNotarial() {
        try {
            FechaHoraSistema fecha = new FechaHoraSistema();
            TipoArchivo tipoArchivo = new TipoArchivo();
            Documento documento = new Documento();
            documento = documentoServicio.buscarPorTramiteRegistros(tramiteSelected.getTramiteId(),
                    ContextoEnum.NOTARIAL.getContexto());
            if (documento != null) {
                String ruta = documento.getRuta();
                int inicio = ruta.lastIndexOf("/");
                String nombre = ruta.substring(inicio + 1);
                byte[] archivoAdjunto = documentoServicio.descargarArchivo(ruta);
                tramiteSelected.setFechaDescarga(fecha.convertirTimestamp(fecha.obtenerFechaHora()));
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

    public void uploadDocumento(FileUploadEvent event) {
        try {
            file = event.getFile();
            fileByte = org.apache.poi.util.IOUtils.toByteArray(file.getInputstream());
            disabledDescargar = Boolean.FALSE;
            disabledFinalizar = Boolean.FALSE;
        } catch (IOException ex) {
            Logger.getLogger(GestionTramiteNotarialCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarLogDescarga(Documento documento) {
        try {
            LogDescarga logDescarga = new LogDescarga();
            logDescarga.setUsuarioId(usuario.getUsuarioId());
            logDescarga.setDocumento(documento);
            logDescarga.setFecha(tramiteSelected.getFechaDescarga());
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

    //Getters & Setters
    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;
    }

    public Tramite getTramiteSelected() {
        return tramiteSelected;
    }

    public void setTramiteSelected(Tramite tramiteSelected) {
        this.tramiteSelected = tramiteSelected;
    }

    public Boolean getOnTamiteSelect() {
        return onTamiteSelect;
    }

    public void setOnTamiteSelect(Boolean onTamiteSelect) {
        this.onTamiteSelect = onTamiteSelect;
    }

    public Short getEstadoTramite() {
        return estadoTramite;
    }

    public void setEstadoTramite(Short estadoTramite) {
        this.estadoTramite = estadoTramite;
    }

    public Boolean getOnInconsistente() {
        return onInconsistente;
    }

    public void setOnInconsistente(Boolean onInconsistente) {
        this.onInconsistente = onInconsistente;
    }

    public Boolean getOnCerrado() {
        return onCerrado;
    }

    public void setOnCerrado(Boolean onCerrado) {
        this.onCerrado = onCerrado;
    }

    public Boolean getDisabledDescargar() {
        return disabledDescargar;
    }

    public void setDisabledDescargar(Boolean disabledDescargar) {
        this.disabledDescargar = disabledDescargar;
    }

    public Boolean getDisabledFinalizar() {
        return disabledFinalizar;
    }

    public void setDisabledFinalizar(Boolean disabledFinalizar) {
        this.disabledFinalizar = disabledFinalizar;
    }

}