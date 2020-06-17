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

import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.modelo.Usuario;


/**
 * The persistent class for the tramite database table.
 * 
 */
@Entity
@Table(name="tramite", schema="ec_dinardap_notarial_registral")
@NamedQuery(name="Tramite.findAll", query="SELECT t FROM Tramite t")
public class Tramite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRAMITE_TRAMITEID_GENERATOR", sequenceName="TRAMITE_TRAMITE_ID_SEQ", schema="ec_dinardap_notarial_registral", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRAMITE_TRAMITEID_GENERATOR")
	@Column(name="tramite_id")
	private Long tramiteId;

	//@Column(name="cerrado_por")
	//private Integer cerradoPor;

	private String codigo;

	@Column(name="condicion_ciudadano_requirente")
	private String condicionCiudadanoRequirente;

	@Column(name="correo_requirente")
	private String correoRequirente;

	private Short estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_cierre")
	private Date fechaCierre;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_descarga")
	private Date fechaDescarga;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="identificacion_requirente")
	private String identificacionRequirente;

	//@Column(name="institucion_id")
	//private Integer institucionId;

	@Column(name="nacionalidad_requirente")
	private String nacionalidadRequirente;

	@Column(name="nombre_requirente")
	private String nombreRequirente;

	private String observacion;

	//@Column(name="registrado_por")
	//private Integer registradoPor;

	//bi-directional many-to-one association to Documento
	@ManyToOne
	@JoinColumn(name="cerrado_por", referencedColumnName = "usuario_id")
	private Usuario cerradoPor;
	
	//bi-directional many-to-one association to Documento
	@ManyToOne
	@JoinColumn(name="institucion_id", referencedColumnName = "institucion_id")
	private Institucion institucion;
	
	//bi-directional many-to-one association to Documento
	@ManyToOne
	@JoinColumn(name="registrado_por", referencedColumnName = "usuario_id")
	private Usuario registradoPor;
	
	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="tramite")
	private List<Documento> documentos;

	//bi-directional many-to-one associatioinn to TipoTramite
	@ManyToOne
	@JoinColumn(name="tipo_tramite_id")
	private TipoTramite tipoTramite;

	//bi-directional many-to-one association to Tramite
	@ManyToOne
	@JoinColumn(name="tra_tramite_id")
	private Tramite tramite;

	//bi-directional many-to-one association to Tramite
	@OneToMany(mappedBy="tramite")
	private List<Tramite> tramites;

	public Tramite() {
	}

	public Long getTramiteId() {
		return this.tramiteId;
	}

	public void setTramiteId(Long tramiteId) {
		this.tramiteId = tramiteId;
	}

	public Usuario getCerradoPor() {
		return this.cerradoPor;
	}

	public void setCerradoPor(Usuario cerradoPor) {
		this.cerradoPor = cerradoPor;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCondicionCiudadanoRequirente() {
		return this.condicionCiudadanoRequirente;
	}

	public void setCondicionCiudadanoRequirente(String condicionCiudadanoRequirente) {
		this.condicionCiudadanoRequirente = condicionCiudadanoRequirente;
	}

	public String getCorreoRequirente() {
		return this.correoRequirente;
	}

	public void setCorreoRequirente(String correoRequirente) {
		this.correoRequirente = correoRequirente;
	}

	public Short getEstado() {
		return this.estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaDescarga() {
		return this.fechaDescarga;
	}

	public void setFechaDescarga(Date fechaDescarga) {
		this.fechaDescarga = fechaDescarga;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getIdentificacionRequirente() {
		return this.identificacionRequirente;
	}

	public void setIdentificacionRequirente(String identificacionRequirente) {
		this.identificacionRequirente = identificacionRequirente;
	}

	public Institucion getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}

	public String getNacionalidadRequirente() {
		return this.nacionalidadRequirente;
	}

	public void setNacionalidadRequirente(String nacionalidadRequirente) {
		this.nacionalidadRequirente = nacionalidadRequirente;
	}

	public String getNombreRequirente() {
		return this.nombreRequirente;
	}

	public void setNombreRequirente(String nombreRequirente) {
		this.nombreRequirente = nombreRequirente;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Usuario getRegistradoPor() {
		return this.registradoPor;
	}

	public void setRegistradoPor(Usuario registradoPor) {
		this.registradoPor = registradoPor;
	}

	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public Documento addDocumento(Documento documento) {
		getDocumentos().add(documento);
		documento.setTramite(this);

		return documento;
	}

	public Documento removeDocumento(Documento documento) {
		getDocumentos().remove(documento);
		documento.setTramite(null);

		return documento;
	}

	public TipoTramite getTipoTramite() {
		return this.tipoTramite;
	}

	public void setTipoTramite(TipoTramite tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	public List<Tramite> getTramites() {
		return this.tramites;
	}

	public void setTramites(List<Tramite> tramites) {
		this.tramites = tramites;
	}

	public Tramite addTramite(Tramite tramite) {
		getTramites().add(tramite);
		tramite.setTramite(this);

		return tramite;
	}

	public Tramite removeTramite(Tramite tramite) {
		getTramites().remove(tramite);
		tramite.setTramite(null);

		return tramite;
	}

}