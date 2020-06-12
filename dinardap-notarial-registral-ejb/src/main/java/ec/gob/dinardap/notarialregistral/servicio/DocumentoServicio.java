package ec.gob.dinardap.notarialregistral.servicio;



import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.persistence.servicio.GenericService;

@Local
public interface DocumentoServicio extends GenericService<Documento, Long> {
	public Documento buscarPorTramiteRegistros(Long tramiteId, Short contexto);
	public byte[] descargarArchivo(String rutaArchivo);
	public String subirArchivos(DocumentoDto documentoDto);

}
