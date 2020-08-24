package ec.gob.dinardap.notarialregistral.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.autorizacion.constante.SemillaEnum;
import ec.gob.dinardap.autorizacion.util.EncriptarCadenas;
import ec.gob.dinardap.seguridad.dto.ValidacionDto;
import ec.gob.dinardap.seguridad.modelo.Institucion;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import ec.gob.dinardap.seguridad.servicio.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import org.primefaces.PrimeFaces;
//import ec.gob.dinardap.turno.modelo.RegistroMercantil;
//import ec.gob.dinardap.turno.modelo.Usuario;
//import ec.gob.dinardap.turno.servicio.RegistroMercantilServicio;
//import ec.gob.dinardap.turno.servicio.UsuarioServicio;

@Named(value = "loginCtrl")
@ViewScoped
public class LoginCtrl extends BaseCtrl {

    private static final long serialVersionUID = 4873444362560469061L;

    @EJB
    private UsuarioServicio usuarioServicio;

    @EJB
    private InstitucionServicio institucionServicio;

    private String nombreUsuario;
    private String contrasena;

    private Institucion institucion;
    private List<Institucion> institucionList;

    private ValidacionDto validacionDto;

    @PostConstruct
    public void init() {
    }

    public List<Institucion> completeNombreInstitucion(String query) {
        List<Institucion> filteredInstitucion = new ArrayList<Institucion>();
        for (Institucion i : institucionList) {
            if (i.getNombre().toLowerCase().contains(query)
                    || i.getNombre().toUpperCase().contains(query)) {
                filteredInstitucion.add(i);
            }
        }
        return filteredInstitucion;
    }

    public void validarUsuario() {
        validacionDto = new ValidacionDto();
        validacionDto = usuarioServicio.validarUsuario(getNombreUsuario(),
                EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getContrasena())),
                Integer.parseInt(getIdentificacionSistema()));
        if (validacionDto != null) {
            setSessionVariable("perfil", validacionDto.getPerfil());
            setSessionVariable("usuarioId", validacionDto.getUsuarioId().toString());
            //Seleccionar Institucion
            String instituciones[] = validacionDto.getInstitucion().split(",");
            institucionList = new ArrayList<Institucion>();
            if (instituciones.length > 1) {
                for (String institucion1 : instituciones) {
                    Institucion i = institucionServicio.findByPk(Integer.parseInt(institucion1.trim()));
                    if (i != null) {
                        institucionList.add(i);
                    }
                }
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('seleccionInstitucionDlg').show();");
            } else {
                setSessionVariable("institucionId", instituciones[0]);
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    limpiarCampos();
                    context.redirect(context.getRequestContextPath() + "/paginas/brand.jsf");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
        }
    }

    public void ingresar() {
        if (institucion.getInstitucionId() != null) {
            setSessionVariable("institucionId", institucion.getInstitucionId().toString());
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                limpiarCampos();
                context.redirect(context.getRequestContextPath() + "/paginas/brand.jsf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", "Seleccionar una Institución"));
        }
    }

    public void validarUsuario1() {
        validacionDto = new ValidacionDto();
        validacionDto = usuarioServicio.validarUsuario(getNombreUsuario(),
                EncriptarCadenas.encriptarCadenaSha1(SemillaEnum.SEMILLA_SEGURIDAD.getSemilla().concat(getContrasena())),
                Integer.parseInt(getIdentificacionSistema()));
        System.out.println("Validacion: " + validacionDto.getInstitucionId());
        if (validacionDto != null) {
            setSessionVariable("perfil", validacionDto.getPerfil());
            //Seleccionar Institucion
            String instituciones[] = validacionDto.getInstitucion().split(",");
            System.out.println("Instituciones: " + instituciones.length);
            if (instituciones.length > 1) {
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('seleccionInstitucionDlg').show();");
            } else {
                setSessionVariable("institucionId", "6");
            }
            //-- 
            setSessionVariable("institucionId", "6");
            setSessionVariable("usuarioId", validacionDto.getUsuarioId().toString());
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                limpiarCampos();
                context.redirect(context.getRequestContextPath() + "/paginas/brand.jsf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Autenticacíon", getBundleMensaje("error.credenciales", null)));
        }
    }

    public void cancelar() {
        setNombreUsuario(null);
        setContrasena(null);
    }

    public void recuperarClave() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/restaurarClave.jsf");
        } catch (IOException ex) {
            Logger.getLogger(LoginCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirDialogo() {

    }

    public void cambiarContrasena() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            //limpiarCampos();
            context.redirect(context.getRequestContextPath() + "/publico/cambiarContrasena.jsf");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        limpiarCampos();
//        setDialogo(Boolean.FALSE);
    }

    public void enviar() {

        /*try {
			Email email = new Email();
			String mensajeMail = "Prueba";
			email.sendMail("leonardo.munoz@dinardap.gob.ec", "Consulta", mensajeMail);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

    private void limpiarCampos() {
//        setNuevaContrasena("");
//        setContrasena("");
//        setCedula("");
//        setEntidad(null);
    }

    // Getters & Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Integer getEntidad() {
//        return entidad;
        return 0;
    }

    public void setEntidad(Integer entidad) {
//        this.entidad = entidad;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<Institucion> getInstitucionList() {
        return institucionList;
    }

    public void setInstitucionList(List<Institucion> institucionList) {
        this.institucionList = institucionList;
    }

}
