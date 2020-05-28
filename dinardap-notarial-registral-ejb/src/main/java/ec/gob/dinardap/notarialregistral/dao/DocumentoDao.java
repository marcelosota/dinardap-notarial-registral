package ec.gob.dinardap.notarialregistral.dao;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.persistence.dao.GenericDao;

@Local
public interface DocumentoDao extends GenericDao<Documento, Long> {

}
