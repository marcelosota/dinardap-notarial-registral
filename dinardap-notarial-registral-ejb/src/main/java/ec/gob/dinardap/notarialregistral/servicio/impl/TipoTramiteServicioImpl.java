package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TipoTramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.TipoTramite;
import ec.gob.dinardap.notarialregistral.servicio.TipoTramiteServicio;
import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "TipoTramiteServicio")
public class TipoTramiteServicioImpl extends GenericServiceImpl<TipoTramite, Integer> implements TipoTramiteServicio {

    @EJB
    private TipoTramiteDao tipoTramiteDao;

    @Override
    public GenericDao<TipoTramite, Integer> getDao() {
        return tipoTramiteDao;
    }

    @Override
    public List<TipoTramite> getTipoTramiteEstado(Short estado) {
        List<TipoTramite> tipoTramiteList = new ArrayList<TipoTramite>();
        String[] criteriaNombres = {"estado"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.SHORT_EQUALS};
        Object[] criteriaValores = {estado};
        String[] orderBy = {"tipoTramiteId"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        tipoTramiteList = findByCriterias(criteria);
        return tipoTramiteList;
    }

}
