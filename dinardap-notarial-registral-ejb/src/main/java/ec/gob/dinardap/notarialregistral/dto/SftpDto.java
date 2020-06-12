/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.notarialregistral.dto;

import ec.gob.dinardap.sftp.util.CredencialesSFTP;

/**
 *
 * @author christian.gaona
 */
public class SftpDto {

    private CredencialesSFTP credencialesSFTP;
    private byte[] archivo;

    public SftpDto() {
        credencialesSFTP = new CredencialesSFTP();
    }

        public CredencialesSFTP getCredencialesSFTP() {
        return credencialesSFTP;
    }

    public void setCredencialesSFTP(CredencialesSFTP credencialesSFTP) {
        this.credencialesSFTP = credencialesSFTP;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

}
