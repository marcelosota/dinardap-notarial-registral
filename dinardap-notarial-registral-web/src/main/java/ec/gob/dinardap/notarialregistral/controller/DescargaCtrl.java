package ec.gob.dinardap.notarialregistral.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.dinardap.notarialregistral.servicio.DocumentoServicio;
import ec.gob.dinardap.seguridad.servicio.ParametroServicio;
import ec.gob.dinardap.util.TipoArchivo;

@Named("descargaCtrl")
@ViewScoped
public class DescargaCtrl extends BaseCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2852165827240555669L;
	
	@EJB
	private ParametroServicio parametroServicio;
	@EJB
	private DocumentoServicio documentoServicio;
	
	private String param;

	public void descargarArchivo(ComponentSystemEvent event){
		TipoArchivo tipoArchivo = new TipoArchivo();
		if(getParam() != null){
			String ruta = parametroServicio.findByPk(getParam()).getValor();
			if(ruta != null || ruta != ""){
				byte[] contenido = documentoServicio.descargarArchivo(ruta);
				downloadFile(contenido, tipoArchivo.obtenerTipoArchivo(ruta), ruta.substring(ruta.lastIndexOf("/") + 1));
			}
		}
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
