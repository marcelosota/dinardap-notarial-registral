package ec.gob.dinardap.notarialregistral.dao.ejb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ec.gob.dinardap.notarialregistral.constante.ContextoEnum;
import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
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
	@SuppressWarnings("unchecked")
	@Override
	public List<TramiteRegistradorDto> misTramites(Short contextoNotarial, Short contextoRegistral, Short estadoTramite, Integer canton) {
		StringBuilder sql = new StringBuilder(" select t.tramite_id, t.codigo, i.nombre as institucion, t.fecha_registro, t.fecha_cierre, t.observacion, ");
		sql.append(" (select d1.ruta as notarial from ec_dinardap_notarial_registral.documento as d1 ");
		sql.append(" where d1.contexto_archivo = ").append(contextoNotarial);
		sql.append(" and d1.estado = 1 ");		 
		sql.append(" group by d1.ruta), (select d2.ruta as registral ");
		sql.append(" from ec_dinardap_notarial_registral.documento as d2 ");
		sql.append(" where d2.contexto_archivo = ").append(contextoRegistral);
		sql.append(" and d2.estado=1 "); 
		sql.append(" group by d2.ruta) ");		
		sql.append(" from ec_dinardap_notarial_registral.tramite t "); 		
		sql.append(" INNER JOIN ec_dinardap_seguridad.institucion i ON t.institucion_id = i.institucion_id "); 
		sql.append(" INNER JOIN ec_dinardap_seguridad.usuario u ON t.registrado_por = u.usuario_id ");
		sql.append(" INNER JOIN ec_dinardap_notarial_registral.documento d ON t.tramite_id = d.tramite_id ");		  		
		sql.append(" where t.estado = ").append(estadoTramite);
		sql.append(" and d.estado = 1 ");
		sql.append(" and i.canton_id = ").append(canton);
		sql.append(" group by t.tramite_id, t.codigo, i.nombre, t.fecha_registro, t.fecha_cierre, t.observacion ");
		sql.append(" order by fecha_registro asc ");				
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> lista = query.getResultList();
		List<TramiteRegistradorDto> misTramites = new ArrayList<TramiteRegistradorDto>();
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
					item.setFechaRegistro(fechaHora.convertirFecha(fila[3].toString()));
				} else {
					item.setFechaRegistro(null);
				}
				if (fila[4] != null) {
					item.setFechaCierre(fechaHora.convertirFecha(fila[4].toString()));
				} else {
					item.setFechaCierre(null);
				}
				if (fila[5] != null) {
					item.setObservacionRegistro(fila[5].toString());
				} else {
					item.setObservacionRegistro(null);
				}
				if (fila[6] != null) {
					item.setRutaNotarial(fila[6].toString());
				} else {
					item.setRutaNotarial(null);
				}
				if (fila[7] != null) {
					item.setRutaRegistral(fila[7].toString());
				} else {
					item.setRutaRegistral(null);
				}

				misTramites.add(item);
			}
		}
		return misTramites;
	}
	
}
