package ec.gob.dinardap.notarialregistral.dto;

import ec.gob.dinardap.notarialregistral.modelo.Documento;

public class DocumentoDto {
	private Documento documento;
	private String nombre;
	private byte[] contenido;
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	
	

}
