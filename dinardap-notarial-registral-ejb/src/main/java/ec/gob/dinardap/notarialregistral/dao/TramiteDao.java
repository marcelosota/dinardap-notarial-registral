package ec.gob.dinardap.notarialregistral.dao;

import java.util.List;

import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import javax.ejb.Local;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.dao.GenericDao;

@Local
public interface TramiteDao extends GenericDao<Tramite, Long> {
	
	public List<TramiteRegistradorDto> tramitesPendientes(Short estadoTramite, Integer canton);
	public List<TramiteRegistradorDto> misTramites(Short contextoNotarial, Short contextoRegistral, Short estadoTramite, Integer usuarioId);	
}
