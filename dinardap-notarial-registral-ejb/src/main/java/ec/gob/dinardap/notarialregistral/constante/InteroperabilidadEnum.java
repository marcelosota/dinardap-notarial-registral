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
public enum InteroperabilidadEnum {
    RC("2936"), RC_PARAM("identicacion"), RC_USUARIO("SnRinTDtd.0620"), RC_CONTRASENA("Xe3=9g2d-kCrS+"),
    SRI("2937"), SRI_PARAM("ruc"), SRI_USUARIO(""), SRI_CONTRASENA(""),;

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
        for (InteroperabilidadEnum e : InteroperabilidadEnum.values()) {
            if (e.getPaquete() == paquete) {
                valor = e;
                break;
            }
        }
        return valor;
    }

}
