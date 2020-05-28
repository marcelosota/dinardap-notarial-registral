package ec.gob.dinardap.notarialregistral.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name="TramiteDao")
public class TramiteDaoEjb extends GenericDaoEjb<Tramite, Long> implements TramiteDao {

	public TramiteDaoEjb() {
		super(Tramite.class);
	}

}
