package ec.gob.dinardap.notarialregistral.controller;

import ec.gob.dinardap.interoperadorv2.cliente.servicio.ServicioDINARDAP;
import ec.gob.dinardap.interoperadorv2.ws.ConsultarResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.notarialregistral.constante.EstadoTipoTramiteEnum;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.constante.TipoIdentificacionEnum;
import ec.gob.dinardap.notarialregistral.constante.InteroperabilidadEnum;
import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TipoTramiteServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@Named(value = "tramiteNotarialCtrl")
@ViewScoped
public class tramiteNotarialCtrl extends BaseCtrl implements Serializable {

    private static final long serialVersionUID = 4955068063614741302L;
    //Declaración de variables
    //Variables de control visual    
    private Boolean onCedula;
    private Boolean onPasaporte;
    private Boolean disableCampos;
    private Boolean tramiteOrigen;

    //Variables de negocio
    private String tipoIdentificacion;
    private String codigoTramiteInicial;
    private Tramite tramite;
    private Tramite tramiteGenerado;
    private Integer institucionId;
    private Integer usuarioId;

    //Listas    
    private List<TipoTramite> tipoTramiteList;
    private List<String> tipoIdentificacionList;

    @EJB
    private TipoTramiteServicio tipoTramiteServicio;

    @EJB
    private TramiteServicio tramiteServicio;

    @PostConstruct
    protected void init() {
//      institucionId = Integer.parseInt(this.getSessionVariable("institucionId")); //con Login
        institucionId = 1;  //Sin Login        
//      usuarioId = Integer.parseInt(this.getSessionVariable("usuarioId")); //con Login
        usuarioId = 1;  //Sin Login        

        tipoIdentificacionList = new ArrayList<String>();
        tipoIdentificacionList = TipoIdentificacionEnum.getTipoIdentificacionList();
        tipoIdentificacion = "";

        if (!TipoIdentificacionEnum.getTipoIdentificacionList().isEmpty()) {
            tipoIdentificacion = TipoIdentificacionEnum.getTipoIdentificacionList().get(0);
        }

        tipoTramiteList = new ArrayList<TipoTramite>();
        tipoTramiteList = tipoTramiteServicio.getTipoTramiteEstado(EstadoTipoTramiteEnum.ACTIVO.getEstado());

        tramite = new Tramite();

        onCedula = Boolean.TRUE;
        onPasaporte = Boolean.FALSE;
        disableCampos = Boolean.TRUE;
        tramiteOrigen = Boolean.FALSE;

    }

    public void blurIdentificacionCedula() {
//        String nombreAux = getNombreCiudadano(tramite.getIdentificacionRequirente());
        String nombreAux = "Christian Gaona";
        if (nombreAux != null) {
            tramite.setNombreRequirente(nombreAux);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", getBundleMensaje("error.cedulaInvalida", null)));
        }
    }

    public List<TipoTramite> completeEstadoTramite(String query) {
        List<TipoTramite> filteredTipoTramite = new ArrayList<TipoTramite>();
        for (TipoTramite tt : tipoTramiteList) {
            if (tt.getNombre().toLowerCase().contains(query)
                    || tt.getNombre().toUpperCase().contains(query)) {
                filteredTipoTramite.add(tt);
            }
        }
        return filteredTipoTramite;
    }

    public void crearTramite() {
        tramiteGenerado = new Tramite();
        tramite.setInstitucionId(institucionId);
        tramite.setFechaRegistro(new Date());
        tramite.setRegistradoPor(1);
        tramite.setEstado(EstadoTramiteEnum.GENERADO.getEstado());
        if (tramiteOrigen) {
            Tramite tramiteOriginal = tramiteServicio.getTramiteByCodigoValidacionTramite(codigoTramiteInicial);
            if (tramiteOriginal.getTramiteId() != null) {
                if (!tramiteServicio.existenciaTramiteAsociado(tramiteOriginal.getTramiteId())) {
                    tramite.setTramite(tramiteOriginal);
                    tramiteServicio.crearTramite(tramite);
                    tramiteGenerado = tramite;
                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('generacionTramiteDlg').show();");
                    tramite = new Tramite();
                    codigoTramiteInicial = null;
                    if (!TipoIdentificacionEnum.getTipoIdentificacionList().isEmpty()) {
                        tipoIdentificacion = TipoIdentificacionEnum.getTipoIdentificacionList().get(0);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", getBundleMensaje("error.tramiteSeguimientoExistente", null) + codigoTramiteInicial));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", getBundleMensaje("error.tramiteSeguimientoInexistente", null)));
            }
        } else {
            tramiteServicio.crearTramite(tramite);
            tramiteGenerado = tramite;
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('generacionTramiteDlg').show();");
            tramite = new Tramite();
            codigoTramiteInicial = null;
            if (!TipoIdentificacionEnum.getTipoIdentificacionList().isEmpty()) {
                tipoIdentificacion = TipoIdentificacionEnum.getTipoIdentificacionList().get(0);
            }
        }
    }

    public void cancelar() {
        tramite = new Tramite();
        codigoTramiteInicial = null;
        if (!TipoIdentificacionEnum.getTipoIdentificacionList().isEmpty()) {
            tipoIdentificacion = TipoIdentificacionEnum.getTipoIdentificacionList().get(0);
        }
    }

    public void onTipoIdentificacion() {
        tramite.setIdentificacionRequirente(null);
        tramite.setNombreRequirente(null);
        tramite.setNacionalidadRequirente(null);

        if (tipoIdentificacion.equals(TipoIdentificacionEnum.CÉDULA.toString())) {
            onCedula = Boolean.TRUE;
            onPasaporte = Boolean.FALSE;
            disableCampos = Boolean.TRUE;
        } else if (tipoIdentificacion.equals(TipoIdentificacionEnum.PASAPORTE.toString())) {
            onCedula = Boolean.FALSE;
            onPasaporte = Boolean.TRUE;
            disableCampos = Boolean.FALSE;
        }
    }

    @SuppressWarnings("unused")
    private String getNombreCiudadano(String cedula) {
        ServicioDINARDAP ob = new ServicioDINARDAP();
        ConsultarResponse objWs;// = new ConsultarResponse();
        String nombreCiudadano = null;
        String razonSocial = null;
        objWs = ob.obtenerDatosFuente(InteroperabilidadEnum.RC_PARAM.getPaquete(), cedula, InteroperabilidadEnum.RC.getPaquete(), InteroperabilidadEnum.RC_USUARIO.getPaquete(), InteroperabilidadEnum.RC_CONTRASENA.getPaquete());
        if (objWs != null) {
            nombreCiudadano = objWs.getPaquete().getEntidades().getEntidad().get(0).getFilas().getFila().get(0).getColumnas().getColumna().get(3).getValor();
        }
        return nombreCiudadano;
    }

    public void onCheckTramiteOrigen() {
        if (!tramiteOrigen) {
            codigoTramiteInicial = null;
        }
    }

    //Getters & Setters    
    public Tramite getTramite() {
        return tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public List<String> getTipoIdentificacionList() {
        return tipoIdentificacionList;
    }

    public void setTipoIdentificacionList(List<String> tipoIdentificacionList) {
        this.tipoIdentificacionList = tipoIdentificacionList;
    }

    public Boolean getOnCedula() {
        return onCedula;
    }

    public void setOnCedula(Boolean onCedula) {
        this.onCedula = onCedula;
    }

    public Boolean getOnPasaporte() {
        return onPasaporte;
    }

    public void setOnPasaporte(Boolean onPasaporte) {
        this.onPasaporte = onPasaporte;
    }

    public Boolean getDisableCampos() {
        return disableCampos;
    }

    public void setDisableCampos(Boolean disableCampos) {
        this.disableCampos = disableCampos;
    }

    public Tramite getTramiteGenerado() {
        return tramiteGenerado;
    }

    public void setTramiteGenerado(Tramite tramiteGenerado) {
        this.tramiteGenerado = tramiteGenerado;
    }

    public Boolean getTramiteOrigen() {
        return tramiteOrigen;
    }

    public void setTramiteOrigen(Boolean tramiteOrigen) {
        this.tramiteOrigen = tramiteOrigen;
    }

    public String getCodigoTramiteInicial() {
        return codigoTramiteInicial;
    }

    public void setCodigoTramiteInicial(String codigoTramiteInicial) {
        this.codigoTramiteInicial = codigoTramiteInicial;
    }

}
