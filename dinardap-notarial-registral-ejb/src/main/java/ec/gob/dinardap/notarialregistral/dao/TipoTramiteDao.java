package ec.gob.dinardap.notarialregistral.dao;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.persistence.dao.GenericDao;

@Local
public interface TipoTramiteDao extends GenericDao<TipoTramite, Integer> {

}
