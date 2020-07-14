package ec.gob.dinardap.notarialregistral.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import ec.gob.dinardap.geografico.modelo.Canton;
import ec.gob.dinardap.geografico.modelo.Provincia;
import ec.gob.dinardap.geografico.servicio.CantonServicio;
import ec.gob.dinardap.geografico.servicio.ProvinciaServicio;
import ec.gob.dinardap.interoperadorv2.cliente.servicio.ServicioDINARDAP;
import ec.gob.dinardap.interoperadorv2.ws.ConsultarResponse;
import ec.gob.dinardap.notarialregistral.constante.EstadoTramiteEnum;
import ec.gob.dinardap.notarialregistral.constante.InteroperabilidadEnum;
import ec.gob.dinardap.notarialregistral.constante.TipoIdentificacionEnum;
import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TipoTramiteServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;

@Named(value = "tramiteNotarialCtrl")
@ViewScoped
public class TramiteNotarialCtrl extends BaseCtrl implements Serializable {

    private static final long serialVersionUID = 4955068063614741302L;
    //Declaración de variables
    //Variables de control visual    
    private Boolean onCedula;
    private Boolean onPasaporte;
    private Boolean disableCampos;
    private Boolean tramiteOrigen;
    private boolean flagCanton;
	private boolean flagInstitucion;

    //Variables de negocio
    private String tipoIdentificacion;
    private String codigoTramiteInicial;
    private Tramite tramite;
    private Tramite tramiteGenerado;
    private Integer institucionId;
    private Integer usuarioId;
    private Integer provinciaId;
    private Integer cantonId;
    private Integer continuaTramiteId;
    //Listas    
    private List<TipoTramite> tipoTramiteList;
    private List<String> tipoIdentificacionList;
    private List<Institucion> listaInstituciones;
	private List<Provincia> provincia;
	private List<Canton> canton;

    @EJB
    private TipoTramiteServicio tipoTramiteServicio;

    @EJB
    private TramiteServicio tramiteServicio;
    
    @EJB
    private InstitucionServicio institucionServicio;
    
    @EJB
    private UsuarioServicio usuariosServicio;
    
    @EJB
    private ProvinciaServicio provinciaServicio;
    
    @EJB
    private CantonServicio cantonServicio;
    

    @PostConstruct
    protected void init() {
    	institucionId = Integer.parseInt(BaseCtrl.getSessionVariable("institucionId")); //con Login
    	usuarioId = Integer.parseInt(BaseCtrl.getSessionVariable("usuarioId")); // con Login
    	
        tipoIdentificacionList = new ArrayList<String>();
        tipoIdentificacionList = TipoIdentificacionEnum.getTipoIdentificacionList();
        tipoIdentificacion = "";

        if (!TipoIdentificacionEnum.getTipoIdentificacionList().isEmpty()) {
            tipoIdentificacion = TipoIdentificacionEnum.getTipoIdentificacionList().get(0);
        }

        //tipoTramiteList = new ArrayList<TipoTramite>();
        //tipoTramiteList = tipoTramiteServicio.getTipoTramiteEstado(EstadoTipoTramiteEnum.ACTIVO.getEstado());

        tramite = new Tramite();

        onCedula = Boolean.TRUE;
        onPasaporte = Boolean.FALSE;
        disableCampos = Boolean.TRUE;
        tramiteOrigen = Boolean.FALSE;
        setFlagCanton(true);
        setFlagInstitucion(true);

    }

    @SuppressWarnings("unused")
	public void blurIdentificacionCedula() {
        String nombreAux = getNombreCiudadano(tramite.getIdentificacionRequirente());
        //String nombreAux = "Chris";
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
        
        tramite.setInstitucion(institucionServicio.findByPk(institucionId));
        tramite.setContinuaTramite(institucionServicio.findByPk(getContinuaTramiteId()));
        tramite.setFechaRegistro(new Date());
        tramite.setRegistradoPor(usuariosServicio.findByPk(getUsuarioId()));
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
    
    public void cantonPorProvincia() {
		if(canton != null && canton.size() > 0)
			canton.clear();
		if(getProvinciaId() != null) {
			setCanton(cantonServicio.buscarCantonesPorProvincia(getProvinciaId()));
			setFlagCanton(false);
		}else {
			setFlagCanton(true);
		}
			
	}
    public void institucionProvinciaCanton() {
    	if(listaInstituciones != null && listaInstituciones.size() > 0)
    		listaInstituciones.clear();
    	if(getProvinciaId() != null && getCantonId() != null) {
    		setListaInstituciones(institucionServicio.obtenerInstitucionPorCantonEstado(getCantonId(), EstadoEnum.ACTIVO.getEstado()));
    		setFlagInstitucion(false);
    	}else
    		setFlagInstitucion(true);
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

    public List<Institucion> getListaInstituciones() {
		return listaInstituciones;
	}

	public void setListaInstituciones(List<Institucion> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
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

    public boolean isFlagCanton() {
		return flagCanton;
	}

	public void setFlagCanton(boolean flagCanton) {
		this.flagCanton = flagCanton;
	}

	public boolean isFlagInstitucion() {
		return flagInstitucion;
	}

	public void setFlagInstitucion(boolean flagInstitucion) {
		this.flagInstitucion = flagInstitucion;
	}

	public String getCodigoTramiteInicial() {
        return codigoTramiteInicial;
    }

    public void setCodigoTramiteInicial(String codigoTramiteInicial) {
        this.codigoTramiteInicial = codigoTramiteInicial;
    }

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Integer getProvinciaId() {
		return provinciaId;
	}

	public void setProvinciaId(Integer provinciaId) {
		this.provinciaId = provinciaId;
	}

	public Integer getCantonId() {
		return cantonId;
	}

	public void setCantonId(Integer cantonId) {
		this.cantonId = cantonId;
	}

	public Integer getContinuaTramiteId() {
		return continuaTramiteId;
	}

	public void setContinuaTramiteId(Integer continuaTramiteId) {
		this.continuaTramiteId = continuaTramiteId;
	}

}
