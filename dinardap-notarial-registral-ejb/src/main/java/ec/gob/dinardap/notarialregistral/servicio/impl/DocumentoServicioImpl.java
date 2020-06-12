package ec.gob.dinardap.notarialregistral.servicio.impl;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.DocumentoDao;
import ec.gob.dinardap.notarialregistral.dto.SftpDto;
import ec.gob.dinardap.notarialregistral.modelo.Documento;
import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.sftp.exception.FtpException;
//import ec.gob.dinardap.sftp.util.CredencialesSFTP;
import ec.gob.dinardap.sftp.util.GestionSFTP;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        /*try {
            sftpDto.setCredencialesSFTP(setCredencialesSftp(sftpDto.getCredencialesSFTP()));
            return GestionSFTP.descargarArchivo(sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(DocumentoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }*/
    	return null;
    }

    /*private CredencialesSFTP setCredencialesSftp(CredencialesSFTP credencialesSFTP) {
        credencialesSFTP.setHost(parametroServicio.findByPk(ParametroEnum.SERVIDOR_SFTP.name()).getValor());
        credencialesSFTP.setPuerto(Integer.parseInt(parametroServicio.findByPk(ParametroEnum.PUERTO_SFTP.name()).getValor()));
        credencialesSFTP.setUsuario(parametroServicio.findByPk(ParametroEnum.SFTP_USUARIO_NOTARIAL_REGISTRAL.name()).getValor());
        credencialesSFTP.setContrasena(parametroServicio.findByPk(ParametroEnum.SFTP_CONTRASENA_NOTARIAL_REGISTRAL.name()).getValor());
        return credencialesSFTP;
    }*/

    @SuppressWarnings("unused")
	private void guardarArchivo(SftpDto sftpDto) {
        try {
            GestionSFTP.subirArchivo(sftpDto.getArchivo(), sftpDto.getCredencialesSFTP());
        } catch (FtpException ex) {
            Logger.getLogger(DocumentoServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
