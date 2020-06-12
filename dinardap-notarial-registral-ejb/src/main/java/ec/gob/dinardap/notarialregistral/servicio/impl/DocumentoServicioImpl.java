package ec.gob.dinardap.notarialregistral.servicio.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.io.FilenameUtils;

import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.util.FechaHoraSistema;
import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;
import ec.gob.dinardap.sftp.exception.FtpException;
import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;

@Stateless(name = "DocumentoServicio")
public class DocumentoServicioImpl extends GenericServiceImpl<Documento, Long> implements DocumentoServicio {

	@EJB
	private DocumentoDao documentoDao;
	@EJB
	private ParametroServicio parametroServicio;

	@Override
	public GenericDao<Documento, Long> getDao() {
		return documentoDao;
	}

	@Override
	public Documento buscarPorTramiteRegistros(Long tramiteId, Short contexto) {
		String[] criteriasPropiedad = { "tramite.tramiteId", "contextoArchivo", "estado" };
		CriteriaTypeEnum[] citeriaOperador = { CriteriaTypeEnum.LONG_EQUALS, CriteriaTypeEnum.SHORT_EQUALS,
				CriteriaTypeEnum.SHORT_EQUALS };
		Object[] criteriaValores = { tramiteId, contexto, EstadoEnum.ACTIVO.getEstado() };
		Criteria criteria = new Criteria(criteriasPropiedad, citeriaOperador, criteriaValores);
		List<Documento> lista = findByCriterias(criteria);
		Documento documento = new Documento();
		if (lista != null && lista.size() > 0) {
			documento = lista.get(0);
			return documento;
		} else {
			return documento=null;
		}
	}
	@Override
	public byte[] descargarArchivo(String rutaArchivo) {
		CredencialesSFTP credenciales = getCredenciales();
		credenciales.setDirOrigen(rutaArchivo);
		try {
			return GestionSFTP.descargarArchivo(credenciales);
		} catch (FtpException e) {
			e.printStackTrace();
			return null;
		}
	}

	private CredencialesSFTP getCredenciales() {
		CredencialesSFTP credenciales = new CredencialesSFTP();
		
		credenciales.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
		credenciales
				.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
		credenciales.setUsuario(
				parametroServicio.findByPk(ParametroEnum.SFTP_USUARIO_NOTARIAL_REGISTRAL.name()).getValor());
		credenciales.setContrasena(
			parametroServicio.findByPk(ParametroEnum.SFTP_CONTRASENA_NOTARIAL_REGISTRAL.name()).getValor());		
		return credenciales;

	}
	@Override
	public String subirArchivos(DocumentoDto documentoDto) {
		CredencialesSFTP credenciales = getCredenciales();
		String nombre = "";		
		
		//nombre = nombre.concat(archivoDto.getArchivo().getBeneficio().getBeneficioId().toString()).concat("_").
				//concat(FechaHoraSistema.obtenerFechaHoraMiliSegundo());
		
		nombre = nombre.concat(".").concat(FilenameUtils.getExtension(documentoDto.getDocumento().getNombreCarga()));
		credenciales.setDirDestino(documentoDto.getDocumento().getRuta().concat(nombre));
		try {
			GestionSFTP.subirArchivo(documentoDto.getContenido(), credenciales);
			documentoDto.getDocumento().setFechaCarga(new Timestamp (new Date().getTime()));
			documentoDto.getDocumento().setRuta(credenciales.getDirDestino());		
			create(documentoDto.getDocumento());
		} catch (FtpException e) {
			e.printStackTrace();
		}
		return credenciales.getDirDestino();
	}

}
