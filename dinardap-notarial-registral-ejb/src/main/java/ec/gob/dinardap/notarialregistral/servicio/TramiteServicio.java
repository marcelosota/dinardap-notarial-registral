package ec.gob.dinardap.notarialregistral.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.servicio.GenericService;

@Local
public interface TramiteServicio extends GenericService<Tramite, Long> {

	public void enviarCorreo();
}
