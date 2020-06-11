package ec.gob.dinardap.notarialregistral.servicio.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;

@Stateless(name="TramiteServicio")
public class TramiteServicioImpl extends GenericServiceImpl<Tramite, Long> implements TramiteServicio {

	@EJB
	private TramiteDao tramiteDao;
	
	//@EJB
	//private MailServicio mailServicio;
	
	@Override
	public GenericDao<Tramite, Long> getDao() {
		return tramiteDao;
	}
	
	@Override
	public void enviarCorreo() {
		/*try {
			MailMessage mailMessage = new MailMessage();
			List<String> to = new ArrayList<String>();
			String asunto = "PRUEBA";
			String mensaje = "Cuerpo del mensaje de prueba";
			to.add("jadira.paspuel@dinardap.gob.ec");
			mailMessage = determinarCredenciales();
			mailMessage.setTo(to);
			mailMessage.setSubject(asunto.toString());
			mailMessage.setText(mensaje);
			mailMessage.setTieneAdjunto(false);
			mailServicio.sendMail(mailMessage);
			System.out.println("Mail enviado");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	/*private MailMessage determinarCredenciales() {
		MailMessage credenciales = new MailMessage();
		credenciales.setFrom("leonardo.munoz@dinardap.gob.ec");
		credenciales.setUsername("leonardo.munoz");
		credenciales.setPassword("G3n3r1ca.321");
		return credenciales;
	}*/

}
