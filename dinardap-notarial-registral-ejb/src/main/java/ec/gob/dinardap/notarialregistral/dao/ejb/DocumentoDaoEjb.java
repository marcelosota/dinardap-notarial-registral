package ec.gob.dinardap.notarialregistral.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name="DocumentoDao")
public class DocumentoDaoEjb extends GenericDaoEjb<Documento, Long> implements DocumentoDao {

	public DocumentoDaoEjb() {
		super(Documento.class);
	}


}
