package ec.gob.dinardap.notarialregistral.dao.ejb;

import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name = "DocumentoDao")
public class DocumentoDaoEjb extends GenericDaoEjb<Documento, Long> implements DocumentoDao {

    public DocumentoDaoEjb() {
        super(Documento.class);
    }

//        public void getTurnos() {
//        Query query = em.createQuery("SELECT t FROM Tramite t WHERE t.institucionId");
//        query.setParameter("dia", turno.getDia());
//        query.setParameter("cedula", turno.getCedula());
//        query.setParameter("registroMercantil", turno.getRegistroMercantil().getRegistroMercantilId());
//        List<Turno> turnoList = new ArrayList<Turno>();
//        turnoList = query.getResultList();
//    }
}
