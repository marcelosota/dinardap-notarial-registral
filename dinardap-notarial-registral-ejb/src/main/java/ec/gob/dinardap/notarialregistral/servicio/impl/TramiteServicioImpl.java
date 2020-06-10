package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "TramiteServicio")
public class TramiteServicioImpl extends GenericServiceImpl<Tramite, Long> implements TramiteServicio {

    @EJB
    private TramiteDao tramiteDao;

    @Override
    public GenericDao<Tramite, Long> getDao() {
        return tramiteDao;
    }

    @Override
    public void crearTramite(Tramite tramite) {
        tramite.setCodigo(GeneradorCodigo.generarCodigo(tramite.getInstitucionId()));
        this.create(tramite);
    }

    @Override
    public Tramite getTramiteByCodigoValidacionTramite(String codigoValidacionTramite) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        Tramite tramite = new Tramite();
        String[] criteriaNombres = {"codigo"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
        Object[] criteriaValores = {codigoValidacionTramite};
        String[] orderBy = {"tramiteId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tramiteList = findByCriterias(criteria);
        if (!tramiteList.isEmpty()) {
            tramite = tramiteList.get(tramiteList.size() - 1);
        }
        return tramite;
    }

    @Override
    public Boolean existenciaTramiteAsociado(Long tramiteId) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        Boolean existenciaTramiteAsociado = Boolean.FALSE;
        String[] criteriaNombres = {"tramite.tramiteId"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.LONG_EQUALS};
        Object[] criteriaValores = {tramiteId};
        String[] orderBy = {"tramiteId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tramiteList = findByCriterias(criteria);
        if (!tramiteList.isEmpty()) {
            existenciaTramiteAsociado = Boolean.TRUE;
        }
        return existenciaTramiteAsociado;
    }

    @Override
    public List<Tramite> getTramiteList(Integer institucionId, Short estado) {
        List<Tramite> tramiteList = new ArrayList<Tramite>();
        Boolean existenciaTramiteAsociado = Boolean.FALSE;
        String[] criteriaNombres = {"institucionId", "estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.SHORT_EQUALS};
        Object[] criteriaValores = {institucionId, estado};
        String[] orderBy = {"tramiteId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        if (!findByCriterias(criteria).isEmpty()) {
            tramiteList = findByCriterias(criteria);
            for (Tramite tramite : tramiteList) {
                if (tramite.getTramite() != null) {
                    tramite.getTramite().getTramiteId();
                }
                if (tramite.getTipoTramite() != null) {
                    tramite.getTipoTramite().getTipoTramiteId();
                }
            }
        }
        return tramiteList;
    }
}
