package ec.gob.dinardap.notarialregistral.servicio.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import ec.gob.dinardap.correo.mdb.cliente.ClienteQueueMailServicio;
import ec.gob.dinardap.correo.servicio.MailServicio;
import ec.gob.dinardap.correo.util.MailMessage;
import ec.gob.dinardap.notarialregistral.constante.ParametroEnum;
import ec.gob.dinardap.notarialregistral.dao.TramiteDao;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.notarialregistral.servicio.TramiteServicio;
import ec.gob.dinardap.notarialregistral.util.GeneradorCodigo;
import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;

@Stateless(name = "TramiteServicio")
public class TramiteServicioImpl extends GenericServiceImpl<Tramite, Long> implements TramiteServicio {

    @EJB
    private TramiteDao tramiteDao;

    //@EJB
    //private ClienteQueueMailServicio clienteQueueMailServicio;
    
    @EJB
    private ParametroServicio parametroServicio;
    
    @EJB
    private MailServicio mailServicio;

    @Override
    public GenericDao<Tramite, Long> getDao() {
        return tramiteDao;
    }

    @Override
    public void crearTramite(Tramite tramite) {
        tramite.setCodigo(GeneradorCodigo.generarCodigo(tramite.getInstitucion().getInstitucionId()));
        this.create(tramite);
    }

    @Override
    public void actuliazarEstadoTramite(Tramite tramite) {
        this.update(tramite);

        String parametroAmbiente = "DESARROLLO";
        MailMessage mailMessage = new MailMessage();

        StringBuilder html = new StringBuilder(200);
        html.append("<br />Estimado/a: <br />");
        html.append("<br /><br />Le informamos que se ha cargado el Acto Notarial para su Trámite con Código de Validación de Trámite Único: ");
        html.append(tramite.getCodigo());
        html.append("<br/>");
        html.append("<br/>Atentamente,<br/>");
        html.append("<br/><FONT COLOR=\"#0000ff\" FACE=\"Arial Narrow, sans-serif\"><B> ");
        html.append("<br/>");
        html.append("SANYR");
        html.append("</B></FONT>");

        List<String> to = new ArrayList<String>();
        StringBuilder asunto = new StringBuilder(200);

        if (parametroAmbiente.equals("PRODUCCION")) {

        } else {
            to.add(tramite.getCorreoRequirente());
            asunto.append("Notificación SANYR");
        }
//        asunto.append("Confirmación de solicitud para categorizar a la empresa: ");
        //mailMessage = new Credenciales().credencialesCorreo();
        mailMessage = credencialesCorreo();
        mailMessage.setTo(to);
        mailMessage.setSubject(asunto.toString());
        mailMessage.setText(html.toString());
        try {
			mailServicio.sendMail(mailMessage);
		} catch (AuthenticationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //clienteQueueMailServicio.encolarMail(mailMessage);
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
        String[] criteriaNombres = {"institucion.institucionId", "estado"};
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

	@Override
	public boolean guardarRegistro(TramiteRegistradorDto tramiteDto) {
		try {
			if (tramiteDto.getTramite() != null) {
				tramiteDto.getTramite().setFechaCierre(new Timestamp(new Date().getTime()));
				tramiteDto.getTramite().setFechaDescarga(tramiteDto.getFechaDescarga());
				tramiteDto.getTramite().setCerradoPor(tramiteDto.getCerradoPor());
				tramiteDto.getTramite().setObservacion(tramiteDto.getObservacionRegistro());
				tramiteDto.getTramite().setEstado(tramiteDto.getEstado());
				update(tramiteDto.getTramite());

			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
	private MailMessage credencialesCorreo() {
        MailMessage credenciales = new MailMessage();
        credenciales.setFrom(parametroServicio.findByPk(ParametroEnum.MAIL_SANYR.name()).getValor());
        credenciales.setUsername(parametroServicio.findByPk(ParametroEnum.MAIL_USERNAME_SANYR.name()).getValor());
        credenciales.setPassword(parametroServicio.findByPk(ParametroEnum.MAIL_CONTRASENA_SANYR.name()).getValor());
        return credenciales;
    }
}
