package ec.gob.dinardap.notarialregistral.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_tramite database table.
 * 
 */
@Entity
@Table(name="tipo_tramite", schema="ec_dinardap_notarial_registral")
@NamedQuery(name="TipoTramite.findAll", query="SELECT t FROM TipoTramite t")
public class TipoTramite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_TRAMITE_TIPOTRAMITEID_GENERATOR", sequenceName="TIPO_TRAMITE_TIPO_TRAMITE_ID_SEQ", schema="ec_dinardap_notarial_registral", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_TRAMITE_TIPOTRAMITEID_GENERATOR")
	@Column(name="tipo_tramite_id")
	private Integer tipoTramiteId;

	private String descripcion;

	private Short estado;

	private String nombre;

	//bi-directional many-to-one association to Tramite
	@OneToMany(mappedBy="tipoTramite")
	private List<Tramite> tramites;

	public TipoTramite() {
	}

	public Integer getTipoTramiteId() {
		return this.tipoTramiteId;
	}

	public void setTipoTramiteId(Integer tipoTramiteId) {
		this.tipoTramiteId = tipoTramiteId;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Short getEstado() {
		return this.estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Tramite> getTramites() {
		return this.tramites;
	}

	public void setTramites(List<Tramite> tramites) {
		this.tramites = tramites;
	}

	public Tramite addTramite(Tramite tramite) {
		getTramites().add(tramite);
		tramite.setTipoTramite(this);

		return tramite;
	}

	public Tramite removeTramite(Tramite tramite) {
		getTramites().remove(tramite);
		tramite.setTipoTramite(null);

		return tramite;
	}

}