/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.constante;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christian.gaona
 */
public enum TipoIdentificacionEnum {

    CÉDULA((String) "Cédula"), PASAPORTE((String) "Pasaporte");
    private String tipoIdentifacion;

    private TipoIdentificacionEnum(String tipoIdentifacion) {
        this.tipoIdentifacion = tipoIdentifacion;
    }

    public static List<String> getTipoIdentificacionList() {
        List<String> tipoIdentificacionList = new ArrayList<String>();
        for (TipoIdentificacionEnum value : TipoIdentificacionEnum.values()) {
            tipoIdentificacionList.add(value.toString());
        }
        return tipoIdentificacionList;
    }
}
