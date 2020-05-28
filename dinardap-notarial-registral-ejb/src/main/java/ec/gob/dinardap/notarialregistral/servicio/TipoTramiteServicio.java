package ec.gob.dinardap.notarialregistral.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.persistence.servicio.GenericService;

@Local
public interface TipoTramiteServicio extends GenericService<TipoTramite, Integer> {

}
