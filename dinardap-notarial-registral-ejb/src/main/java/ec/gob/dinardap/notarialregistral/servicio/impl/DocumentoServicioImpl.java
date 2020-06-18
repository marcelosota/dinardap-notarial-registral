package ec.gob.dinardap.notarialregistral.servicio.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.io.FilenameUtils;

import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;

import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.dto.SftpDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.notarialregistral.util.FechaHoraSistema;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.constante.EstadoEnum;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
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
	public void crearDocumento(Documento documento, SftpDto sftpDto) {
//        sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
		this.create(documento);
//        guardarArchivo(sftpDto);
	}

	@Override
	public byte[] descargarArchivo(SftpDto sftpDto) {
		try {
			sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
			return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
		} catch (FtpException ex) {
			Logger.getLogger(DocumentoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private CredencialesSFTP setCredencialesSftp(CredencialesSFTP credencialesSFTP) {
		credencialesSFTP.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
		credencialesSFTP
				.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
		credencialesSFTP.setUsuario(
				parametroServicio.findByPk(ParametroEnum.SFTP_USUARIO_NOTARIAL_REGISTRAL.name()).getValor());
		credencialesSFTP.setContrasena(
				parametroServicio.findByPk(ParametroEnum.SFTP_CONTRASENA_NOTARIAL_REGISTRAL.name()).getValor());
		return credencialesSFTP;
	}

	@SuppressWarnings("unused")
	private void guardarArchivo(SftpDto sftpDto) {
		try {
			GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
		} catch (FtpException ex) {
			Logger.getLogger(DocumentoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

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
			return documento = null;
		}
	}

	@Override
	public byte[] descargarArchivo(String rutaArchivo) {
		CredencialesSFTP credenciales = getCredenciales();
		credenciales.setDirOrigen(rutaArchivo);
		System.out.println("ruta"+rutaArchivo);
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
	public Boolean subirArchivos(DocumentoDto documentoDto) {
		CredencialesSFTP credenciales = getCredenciales();
		String nombre = "";
		String codigo = "";

		codigo = GeneradorCodigo.generarCodigo(documentoDto.getDocumento().getTramite().getInstitucion().getInstitucionId());
		nombre = codigo.concat(".").concat(FilenameUtils.getExtension(documentoDto.getDocumento().getNombreCarga()));
		System.out.println("nombre"+nombre);
	
		credenciales.setDirDestino(documentoDto.getDocumento().getRuta().concat(nombre));
		try {
			GestionSFTP.subirArchivo(documentoDto.getContenido(), credenciales);
			documentoDto.getDocumento().setFechaCarga(new Timestamp(new Date().getTime()));
			documentoDto.getDocumento().setRuta(credenciales.getDirDestino());
			create(documentoDto.getDocumento());
			return true;
		} catch (FtpException e) {
			e.printStackTrace();
			return false;
		}		
	}

}
