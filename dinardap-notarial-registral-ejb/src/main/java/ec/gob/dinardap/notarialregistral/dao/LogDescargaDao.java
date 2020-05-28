package ec.gob.dinardap.notarialregistral.dao;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.persistence.dao.GenericDao;

@Local
public interface LogDescargaDao extends GenericDao<LogDescarga, Integer> {

}
