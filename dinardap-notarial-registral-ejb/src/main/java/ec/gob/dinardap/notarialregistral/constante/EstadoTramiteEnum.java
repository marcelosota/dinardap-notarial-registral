package ec.gob.dinardap.notarialregistral.constante;


public enum EstadoTramiteEnum {

	GENERADO((short) 1), CARGADO((short) 2), DESCARGADO((short) 3), INCONSISTENTE((short) 4), CERRADO((short) 5);
	
	private Short estado;

	private EstadoTramiteEnum(Short estado) {
		this.estado = estado;
	}

	public Short getEstado() {
		return estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}
	
	public static EstadoTramiteEnum obtenerEstadoPorCodigo(Short estado) {
		EstadoTramiteEnum valor = null;
		for (EstadoTramiteEnum e : EstadoTramiteEnum.values()) {
			if (e.getEstado() == estado) {
				valor = e;
				break;
			}
		}
		return valor;
	}
	
}
