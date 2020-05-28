package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.LogDescargaDao;
import ec.gob.dinardap.notarialregistral.modelo.LogDescarga;
import ec.gob.dinardap.notarialregistral.servicio.LogDescargaServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="LosDescargaServicio")
public class LogDescargaServicioImpl extends GenericServiceImpl<LogDescarga, Integer> implements LogDescargaServicio {

	@EJB
	private LogDescargaDao logDescargaDao;
	
	@Override
	public GenericDao<LogDescarga, Integer> getDao() {
		return logDescargaDao;
	}

}
