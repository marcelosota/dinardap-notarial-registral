package ec.gob.dinardap.notarialregistral.dao;

import java.util.List;

import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.dao.GenericDao;

public interface TramiteDao extends GenericDao<Tramite, Long> {
	
	public List<TramiteRegistradorDto> tramitesPendientes(Short estadoTramite, Integer canton);

}
