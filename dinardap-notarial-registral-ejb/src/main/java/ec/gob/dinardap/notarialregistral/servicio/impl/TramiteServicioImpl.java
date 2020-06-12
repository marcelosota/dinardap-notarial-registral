package ec.gob.dinardap.notarialregistral.servicio.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;


import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="TramiteServicio")
public class TramiteServicioImpl extends GenericServiceImpl<Tramite, Long> implements TramiteServicio {

	@EJB
	private TramiteDao tramiteDao;
	@EJB
	private DocumentoServicio documentoServicio;
	
	@Override
	public GenericDao<Tramite, Long> getDao() {
		return tramiteDao;
	}
	@Override
	public boolean guardarRegistro(TramiteRegistradorDto tramiteDto, DocumentoDto documentoDto) {
		try {
			/*
			Beneficio beneficio = new Beneficio();
			if (beneficioDto.getBeneficioId() != null) {
				beneficio.setBeneficioId(beneficioDto.getBeneficioId());
				beneficio.setInstitucion(beneficioDto.getInstitucion());
				beneficio.setTipoNaturaleza(beneficioDto.getTipoNaturaleza());
				beneficio.setNombre(beneficioDto.getNombre());
				beneficio.setComponenteBeneficio(beneficioDto.getComponenteBeneficio());
				beneficio.setCoberturaGeografica(beneficioDto.getCoberturaGeografica());
				beneficio.setTratamientoEspecial(beneficioDto.getTratamientoEspecial());
				beneficio.setNotaComplementaria(beneficioDto.getNotaComplementaria());
				beneficio.setPeriodicidad(beneficioDto.getPeriodicidad());
				beneficio.setFechaVigencia(beneficioDto.getFechaVigencia());
				if (beneficioDto.getFechaCaducidad() != null)
					beneficio.setFechaCaducidad(beneficioDto.getFechaCaducidad());
				beneficio.setCreadoPor(beneficioDto.getCreadoPor());				
				beneficio.setAprobadoPor(beneficioDto.getAprobadoPor());
				beneficio.setFechaRegistro(beneficioDto.getFechaRegistro());
				beneficio.setFechaAprobacion(beneficioDto.getFechaAprobacion());				
				beneficio.setUltimoProcesamiento(beneficioDto.getUltimoProcesamiento());		
				
				beneficio.setFechaActualizacion(new Timestamp(new Date().getTime()));
				beneficio.setEstado(EstadoBeneficioEnum.PENDIENTE.getEstado());
				update(beneficio);
				*/

				// Subir archivos
				
				
					if(documentoDto.getContenido()!=null)	
						documentoServicio.subirArchivos(documentoDto);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
