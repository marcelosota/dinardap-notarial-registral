package ec.gob.dinardap.notarialregistral.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.persistence.servicio.GenericService;

@Local
public interface LogDescargaServicio extends GenericService<LogDescarga, Integer> {

}
