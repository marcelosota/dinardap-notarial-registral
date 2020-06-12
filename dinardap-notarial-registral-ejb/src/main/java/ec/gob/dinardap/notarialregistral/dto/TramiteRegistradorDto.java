package ec.gob.dinardap.notarialregistral.dto;

import java.util.Date;

import ec.gob.dinardap.notarialregistral.modelo.Tramite;

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
	private Tramite tramite;
	
	
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
	
	
}
