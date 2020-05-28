package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="DocumentoServicio")
public class DocumentoServicioImpl extends GenericServiceImpl<Documento, Long> implements DocumentoServicio {

	@EJB
	private DocumentoDao documentoDao;

	@Override
	public GenericDao<Documento, Long> getDao() {
		return documentoDao;
	}

}
