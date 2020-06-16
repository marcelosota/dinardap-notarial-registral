/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.constante;

/**
 *
 * @author christian.gaona
 */
public enum EstadoTipoTramiteEnum {

    ACTIVO((short) 1), INACTIVO((short) 0);
    private Short estado;

    private EstadoTipoTramiteEnum(Short estado) {
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
