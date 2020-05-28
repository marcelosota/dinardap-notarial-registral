package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TipoTramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.notarialregistral.servicio.TipoTramiteServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="TipoTramiteServicio")
public class TipoTramiteServicioImpl extends GenericServiceImpl<TipoTramite, Integer> implements TipoTramiteServicio {

	@EJB
	private TipoTramiteDao tipoTramiteDao;

	@Override
	public GenericDao<TipoTramite, Integer> getDao() {
		return tipoTramiteDao;
	}

}
