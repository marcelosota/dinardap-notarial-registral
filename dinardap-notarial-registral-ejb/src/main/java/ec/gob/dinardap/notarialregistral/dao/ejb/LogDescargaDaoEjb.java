package ec.gob.dinardap.notarialregistral.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.LogDescargaDao;
import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name="LogDescargaDao")
public class LogDescargaDaoEjb extends GenericDaoEjb<LogDescarga, Integer> implements LogDescargaDao {

	public LogDescargaDaoEjb() {
		super(LogDescarga.class);
	}

}
