package ec.gob.dinardap.notarialregistral.constante;

public enum InteroperabilidadEnum {
	RC("2936"), RC_PARAM("identificacion"), RC_USUARIO("SnRinTDtd.0620"), RC_CONTRASENA("Xe3=9g2d-kCrS+"), 
	SRI("2937"), SRI_PARAM("ruc"), SRI_USUARIO("SnRinTDtd.0620"), SRI_CONTRASENA("Xe3=9g2d-kCrS+");
	
	private String paquete;

	private InteroperabilidadEnum(String paquete) {
		this.paquete = paquete;
	}

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}
	
	public static InteroperabilidadEnum obtenerPaquetePorCodigo(String paquete) {
		InteroperabilidadEnum valor = null;
		for(InteroperabilidadEnum e : InteroperabilidadEnum.values()) {
			if(e.getPaquete() == paquete) {
				valor = e;
				break;
			}
		}
		return valor;
	}

}
