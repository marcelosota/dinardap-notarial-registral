package ec.gob.dinardap.notarialregistral.dao;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.dao.GenericDao;

@Local
public interface TramiteDao extends GenericDao<Tramite, Long> {

}
