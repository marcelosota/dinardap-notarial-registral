package ec.gob.dinardap.notarialregistral.dao.ejb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.util.FechaHoraSistema;
import ec.gob.dinardap.persistence.dao.ejb.GenericDaoEjb;

@Stateless(name="TramiteDao")
public class TramiteDaoEjb extends GenericDaoEjb<Tramite, Long> implements TramiteDao {

	public TramiteDaoEjb() {
		super(Tramite.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TramiteRegistradorDto> tramitesPendientes(Short estadoTramite, Integer canton) {
		StringBuilder sql = new StringBuilder("select t.tramite_id, t.codigo, i.nombre as institucion, u.nombre as registradoPor, t.fecha_registro ");		
		sql.append(" from ec_dinardap_notarial_registral.tramite t ");		
		sql.append(" INNER JOIN ec_dinardap_seguridad.institucion i ON t.institucion_id = i.institucion_id ");
		sql.append(" INNER JOIN ec_dinardap_seguridad.usuario u ON t.registrado_por = u.usuario_id ");		
		sql.append(" where t.estado = ").append(estadoTramite);
		sql.append(" and i.canton_id= ").append(canton);;
		sql.append("  order by fecha_registro ");		
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> lista = query.getResultList();
		List<TramiteRegistradorDto> tramitesPendientes = new ArrayList<TramiteRegistradorDto>();
		FechaHoraSistema fechaHora = new FechaHoraSistema();
		if (lista != null && lista.size() > 0) {
			for (Object[] fila : lista) {
				TramiteRegistradorDto item = new TramiteRegistradorDto();
				if (fila[0] != null) {
					item.setTramiteId(Long.parseLong(fila[0].toString()));					
				} else {
					item.setTramiteId(null);
				}
				if (fila[1] != null) {
					item.setCodigo(fila[1].toString());					
				} else {
					item.setCodigo(null);
				}				
				if (fila[2] != null) {
					item.setInstitucion(fila[2].toString());
				} else {
					item.setInstitucion(null);
				}
				if (fila[3] != null) {
					item.setRegistradoPor(fila[3].toString());
				} else {
					item.setRegistradoPor(null);
				}
				if (fila[4] != null) {
					item.setFechaRegistro(fechaHora.convertirFecha(fila[4].toString()));
				} else {
					item.setFechaRegistro(null);
				}

				tramitesPendientes.add(item);
			}
		}
		return tramitesPendientes;
	}

}
