package ec.gob.dinardap.notarialregistral.dto;

import java.util.Date;

import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.seguridad.modelo.Usuario;

public class TramiteRegistradorDto {
	
	private Long tramiteId;
	private String codigo;
	private String tipoTramite;
	private String institucion;
	private String registradoPor;
	private Date fechaRegistro;
	private String identificacionRequirente;
	private String nombreRequirente;
	private String observacionRegistro;
	private Date fechaCierre;
	private Date fechaDescarga;
	private Usuario cerradoPor;
	private Short estado;
	private Tramite tramite;
	private String rutaNotarial;
	private String rutaRegistral;
	private String descripcionNotarial;
	private boolean documentoNotarial;
	private boolean documentoRegistral;
	
	
	
	public Long getTramiteId() {
		return tramiteId;
	}
	public void setTramiteId(Long tramiteId) {
		this.tramiteId = tramiteId;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	public String getInstitucion() {
		return institucion;
	}
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	public String getRegistradoPor() {
		return registradoPor;
	}
	public void setRegistradoPor(String registradoPor) {
		this.registradoPor = registradoPor;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getIdentificacionRequirente() {
		return identificacionRequirente;
	}
	public void setIdentificacionRequirente(String identificacionRequirente) {
		this.identificacionRequirente = identificacionRequirente;
	}
	public String getNombreRequirente() {
		return nombreRequirente;
	}
	public void setNombreRequirente(String nombreRequirente) {
		this.nombreRequirente = nombreRequirente;
	}
	public String getObservacionRegistro() {
		return observacionRegistro;
	}
	public void setObservacionRegistro(String observacionRegistro) {
		this.observacionRegistro = observacionRegistro;
	}
	public Tramite getTramite() {
		return tramite;
	}
	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public Date getFechaDescarga() {
		return fechaDescarga;
	}
	public void setFechaDescarga(Date fechaDescarga) {
		this.fechaDescarga = fechaDescarga;
	}	
	public Usuario getCerradoPor() {
		return cerradoPor;
	}
	public void setCerradoPor(Usuario cerradoPor) {
		this.cerradoPor = cerradoPor;
	}
	public Short getEstado() {
		return estado;
	}
	public void setEstado(Short estado) {
		this.estado = estado;
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
	public String getDescripcionNotarial() {
		return descripcionNotarial;
	}
	public void setDescripcionNotarial(String descripcionNotarial) {
		this.descripcionNotarial = descripcionNotarial;
	}
	public boolean isDocumentoNotarial() {
		return documentoNotarial;
	}
	public void setDocumentoNotarial(boolean documentoNotarial) {
		this.documentoNotarial = documentoNotarial;
	}
	public boolean isDocumentoRegistral() {
		return documentoRegistral;
	}
	public void setDocumentoRegistral(boolean documentoRegistral) {
		this.documentoRegistral = documentoRegistral;
	}	
	
	
}
