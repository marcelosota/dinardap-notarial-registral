package ec.gob.dinardap.notarialregistral.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TipoTramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name="TipoTramiteDao")
public class TipoTramiteDaoEjb extends GenericDaoEjb<TipoTramite, Integer> implements TipoTramiteDao {

	public TipoTramiteDaoEjb() {
		super(TipoTramite.class);
	}

}
