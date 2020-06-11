package ec.gob.dinardap.notarialregistral.dto;

public class UsuarioDto {

	private Integer usuarioId;
	private String cedula;
	private String nombre;
	private String cargo;
	private String correoElectronico;
	private String telefono;
	private String contrasena;
	private Integer provinciaId;
	private Integer cantonId;
	private Integer tipoInstitucionId;
	private Integer institucionId;
	private Integer perfilId;
	private Short estado;
	
	public Integer getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getContrasena() {
		return contrasena;
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
	public Integer getTipoInstitucionId() {
		return tipoInstitucionId;
	}
	public void setTipoInstitucionId(Integer tipoInstitucionId) {
		this.tipoInstitucionId = tipoInstitucionId;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public Integer getInstitucionId() {
		return institucionId;
	}
	public void setInstitucionId(Integer institucionId) {
		this.institucionId = institucionId;
	}
	public Integer getPerfilId() {
		return perfilId;
	}
	public void setPerfilId(Integer perfilId) {
		this.perfilId = perfilId;
	}
	public Short getEstado() {
		return estado;
	}
	public void setEstado(Short estado) {
		this.estado = estado;
	}
}
