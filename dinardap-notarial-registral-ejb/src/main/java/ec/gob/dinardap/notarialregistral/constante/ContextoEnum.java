package ec.gob.dinardap.notarialregistral.constante;

public enum ContextoEnum {

	NOTARIAL((short) 1), REGISTRAL((short) 2);
	
	private Short contexto;

	private ContextoEnum(Short contexto) {
		this.contexto = contexto;
	}

	public Short getContexto() {
		return contexto;
	}

	public void setContexto(Short contexto) {
		this.contexto = contexto;
	}
	
	public static ContextoEnum obtenerContextoPorCodigo(Short contexto) {
		ContextoEnum valor = null;
		for (ContextoEnum e : ContextoEnum.values()) {
			if (e.getContexto() == contexto) {
				valor = e;
				break;
			}
		}
		return valor;
	}
}
