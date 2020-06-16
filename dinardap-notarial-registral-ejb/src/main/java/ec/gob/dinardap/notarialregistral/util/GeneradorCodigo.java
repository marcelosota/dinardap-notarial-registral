/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.util;

/**
 *
 * @author christian.gaona
 */
public class GeneradorCodigo {

    public static String generarCodigo(Integer institucionId) {
        String codigoGenerado;
        String separador = "-";
        long epoch = System.currentTimeMillis() / 1000;
        codigoGenerado = Long.toHexString(epoch).toUpperCase().concat(separador)
                .concat(generarAleatorio()).concat(separador)
                .concat(generarCodigoInstitucion(institucionId));
        return codigoGenerado;
    }

    private static String generarCodigoInstitucion(Integer institucionId) {
        int totalDigitos = 4;
        return String.format("%0" + totalDigitos + "d", institucionId);
    }

    private static String generarAleatorio() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String claveGenerada = "";
        int numero;
        for (Integer i = 0; i < 6; i++) {
            numero = (int) (Math.random() * 36);
            claveGenerada = claveGenerada + str.substring(numero, numero + 1);
        }
        return claveGenerada;
    }

}
