package ec.gob.dinardap.notarialregistral.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the log_descarga database table.
 * 
 */
@Entity
@Table(name="log_descarga", schema="ec_dinardap_notarial_registral")
@NamedQuery(name="LogDescarga.findAll", query="SELECT l FROM LogDescarga l")
public class LogDescarga implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOG_DESCARGA_LOGDESCARGA_GENERATOR", sequenceName="LOG_DESCARGA_LOG_DESCARGA_SEQ", schema="ec_dinardap_notarial_registral", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_DESCARGA_LOGDESCARGA_GENERATOR")
	@Column(name="log_descarga")
	private Integer logDescarga;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private String responsable;

	@Column(name="usuario_id")
	private Integer usuarioId;

	//bi-directional many-to-one association to Documento
	@ManyToOne
	@JoinColumn(name="documento_id")
	private Documento documento;

	public LogDescarga() {
	}

	public Integer getLogDescarga() {
		return this.logDescarga;
	}

	public void setLogDescarga(Integer logDescarga) {
		this.logDescarga = logDescarga;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getResponsable() {
		return this.responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Integer getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Documento getDocumento() {
		return this.documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}