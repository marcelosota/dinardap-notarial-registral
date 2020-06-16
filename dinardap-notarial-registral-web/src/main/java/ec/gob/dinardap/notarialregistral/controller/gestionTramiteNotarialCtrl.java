package ec.gob.dinardap.notarialregistral.controller;

import ec.gob.dinardap.notarialregistral.constante.ContextoEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoDocumentoEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.dto.SftpDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import java.io.IOException;
import java.io.Serializable;
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
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named(value = "gestionTramiteNotarialCtrl")
@ViewScoped
public class gestionTramiteNotarialCtrl extends BaseCtrl implements Serializable {

    private static final long serialVersionUID = 4955068063614741302L;
    //Declaración de variables
    //Variables de control visual    
    private Boolean onTramiteSelecccionado;
    private Boolean disabledDescargar;

    //Variables de negocio
    private Integer institucionId;
    private Integer usuarioId;
    private Tramite tramiteSeleccionado;
    private UploadedFile file;
    private byte[] fileByte;

    //Listas    
    private List<Tramite> tramiteList;

    @EJB
    private TramiteServicio tramiteServicio;
    @EJB
    private DocumentoServicio documentoServicio;

    @PostConstruct
    protected void init() {
//      institucionId = Integer.parseInt(this.getSessionVariable("institucionId")); //con Login
        institucionId = 1;  //Sin Login        
//      usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId")); //con Login
        usuarioId = 1;  //Sin Login        
        tramiteList = new ArrayList<Tramite>();
        tramiteList = tramiteServicio.getTramiteList(institucionId, EstadoTramiteEnum.GENERADO.getEstado());

        tramiteSeleccionado = new Tramite();
        onTramiteSelecccionado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;

    }

    public void onSelectTramite() {
        onTramiteSelecccionado = Boolean.TRUE;
        disabledDescargar = Boolean.TRUE;
        fileByte = null;
    }

    public void uploadDocumento(FileUploadEvent event) {
        try {
            file = event.getFile();
            fileByte = IOUtils.toByteArray(file.getInputstream());
            disabledDescargar = Boolean.FALSE;
        } catch (IOException ex) {
            Logger.getLogger(gestionTramiteNotarialCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void visualizarArchivo() {
        if (fileByte != null) {
            downloadFile(fileByte,
                    file.getContentType(),
                    file.getFileName());
        }
    }

    public void finalizarTramite() {
        Documento documento = new Documento();
        documento.setTramite(tramiteSeleccionado);
        documento.setRuta(getRuta());
        documento.setFechaCarga(new Date());
        documento.setContextoArchivo(ContextoEnum.NOTARIAL.getContexto());
        documento.setNombreCarga(file.getFileName());
        documento.setSubidoPor(usuarioId);
        documento.setEstado(EstadoDocumentoEnum.ACTIVO.getEstado());

        tramiteSeleccionado.setEstado(EstadoTramiteEnum.CARGADO.getEstado());

        SftpDto sftpDto = new SftpDto();
        sftpDto.getCredencialesSFTP().setDirDestino(documento.getRuta());
        sftpDto.setArchivo(fileByte);

        documentoServicio.crearDocumento(documento, sftpDto);
        tramiteServicio.update(tramiteSeleccionado);

        tramiteList = new ArrayList<Tramite>();
        tramiteList = tramiteServicio.getTramiteList(institucionId, EstadoTramiteEnum.GENERADO.getEstado());

        fileByte = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", getBundleMensaje("mensaje.tramiteFinalizado", null)));
        
        tramiteSeleccionado = new Tramite();
        onTramiteSelecccionado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;

    }

    public void cancelar() {
        tramiteSeleccionado = new Tramite();
        onTramiteSelecccionado = Boolean.FALSE;
        disabledDescargar = Boolean.TRUE;
    }

    private String getRuta() {
//        /notarial/2020/6/8/ID_INSTITUCION/5ED9548B-3MFNLN-0001.pdf
        Calendar calendar = Calendar.getInstance();
        String ruta = "/";
        ruta += ContextoEnum.NOTARIAL.name() + "/";
        ruta += calendar.get(Calendar.YEAR) + "/";
        ruta += (calendar.get(Calendar.MONTH) + 1) + "/";
        ruta += calendar.get(Calendar.DAY_OF_MONTH) + "/";
        ruta += institucionId + "/";
        ruta += tramiteSeleccionado.getCodigo() + "." + file.getContentType().split("\\/")[1];
        return ruta;
    }

    //Getters & Setters    
    public List<Tramite> getTramiteList() {
        return tramiteList;
    }

    public void setTramiteList(List<Tramite> tramiteList) {
        this.tramiteList = tramiteList;

    }

    public Tramite getTramiteSeleccionado() {
        return tramiteSeleccionado;
    }

    public void setTramiteSeleccionado(Tramite tramiteSeleccionado) {
        this.tramiteSeleccionado = tramiteSeleccionado;
    }

    public Boolean getOnTramiteSelecccionado() {
        return onTramiteSelecccionado;
    }

    public void setOnTramiteSelecccionado(Boolean onTramiteSelecccionado) {
        this.onTramiteSelecccionado = onTramiteSelecccionado;
    }

    public Boolean getDisabledDescargar() {
        return disabledDescargar;
    }

    public void setDisabledDescargar(Boolean disabledDescargar) {
        this.disabledDescargar = disabledDescargar;
    }

}
