package ec.gob.dinardap.notarialregistral.constante;

public enum EstadoDocumentoEnum {

    ACTIVO((short) 1), INACTIVO((short) 0);

    private Short estado;

    private EstadoDocumentoEnum(Short estado) {
        this.estado = estado;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public static EstadoDocumentoEnum obtenerEstadoPorCodigo(Short estado) {
        EstadoDocumentoEnum valor = null;
        for (EstadoDocumentoEnum e : EstadoDocumentoEnum.values()) {
            if (e.getEstado() == estado) {
                valor = e;
                break;
            }
        }
        return valor;
    }

}
