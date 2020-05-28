package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="TramiteServicio")
public class TramiteServicioDao extends GenericServiceImpl<Tramite, Long> implements TramiteServicio {

	@EJB
	private TramiteDao tramiteDao;
	
	@Override
	public GenericDao<Tramite, Long> getDao() {
		return tramiteDao;
	}

}
