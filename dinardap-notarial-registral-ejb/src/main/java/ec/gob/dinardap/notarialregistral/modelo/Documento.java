package ec.gob.dinardap.notarialregistral.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the documento database table.
 * 
 */
@Entity
@Table(name="documento", schema="ec_dinardap_notarial_registral")
@NamedQuery(name="Documento.findAll", query="SELECT d FROM Documento d")
public class Documento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DOCUMENTO_DOCUMENTOID_GENERATOR", sequenceName="DOCUMENTO_DOCUMENTO_ID_SEQ", schema="ec_dinardap_notarial_registral", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DOCUMENTO_DOCUMENTOID_GENERATOR")
	@Column(name="documento_id")
	private Long documentoId;

	@Column(name="contexto_archivo")
	private Short contextoArchivo;

	private Short estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_carga")
	private Date fechaCarga;

	@Column(name="nombre_carga")
	private String nombreCarga;

	private String ruta;

	@Column(name="subido_por")
	private Integer subidoPor;

	//bi-directional many-to-one association to Tramite
	@ManyToOne
	@JoinColumn(name="tramite_id")
	private Tramite tramite;

	//bi-directional many-to-one association to LogDescarga
	@OneToMany(mappedBy="documento")
	private List<LogDescarga> logDescargas;

	public Documento() {
	}

	public Long getDocumentoId() {
		return this.documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public Short getContextoArchivo() {
		return this.contextoArchivo;
	}

	public void setContextoArchivo(Short contextoArchivo) {
		this.contextoArchivo = contextoArchivo;
	}

	public Short getEstado() {
		return this.estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

	public Date getFechaCarga() {
		return this.fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public String getNombreCarga() {
		return this.nombreCarga;
	}

	public void setNombreCarga(String nombreCarga) {
		this.nombreCarga = nombreCarga;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Integer getSubidoPor() {
		return this.subidoPor;
	}

	public void setSubidoPor(Integer subidoPor) {
		this.subidoPor = subidoPor;
	}

	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	public List<LogDescarga> getLogDescargas() {
		return this.logDescargas;
	}

	public void setLogDescargas(List<LogDescarga> logDescargas) {
		this.logDescargas = logDescargas;
	}

	public LogDescarga addLogDescarga(LogDescarga logDescarga) {
		getLogDescargas().add(logDescarga);
		logDescarga.setDocumento(this);

		return logDescarga;
	}

	public LogDescarga removeLogDescarga(LogDescarga logDescarga) {
		getLogDescargas().remove(logDescarga);
		logDescarga.setDocumento(null);

		return logDescarga;
	}

}