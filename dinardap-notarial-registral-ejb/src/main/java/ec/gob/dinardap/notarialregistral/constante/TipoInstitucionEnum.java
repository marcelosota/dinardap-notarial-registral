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
public enum TipoInstitucionEnum {

    REGISTRO_MERCANTIL((Integer) 4), REGISTRO_PROPIEDAD((Integer) 5), REGISTRO_MIXTO((Integer) 6);
    private final Integer tipoInstitucion;

    private TipoInstitucionEnum(Integer tipoInstitucion) {
        this.tipoInstitucion = tipoInstitucion;
    }

    public Integer getTipoInstitucion() {
        return tipoInstitucion;
    }

    public static List<Integer> getTipoInstitucionList() {
        List<Integer> tipoInstitucionList = new ArrayList<Integer>();
        for (TipoInstitucionEnum value : TipoInstitucionEnum.values()) {
            tipoInstitucionList.add(value.tipoInstitucion);
        }
        return tipoInstitucionList;
    }
}
