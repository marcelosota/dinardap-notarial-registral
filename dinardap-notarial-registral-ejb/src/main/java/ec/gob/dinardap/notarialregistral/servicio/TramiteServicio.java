package ec.gob.dinardap.notarialregistral.servicio;

import javax.ejb.Local;

import ec.gob.dinardap.notarialregistral.dto.DocumentoDto;
import ec.gob.dinardap.notarialregistral.dto.TramiteRegistradorDto;
import ec.gob.dinardap.notarialregistral.modelo.Tramite;
import ec.gob.dinardap.persistence.servicio.GenericService;
import java.util.List;

@Local
public interface TramiteServicio extends GenericService<Tramite, Long> {
	public boolean guardarRegistro(TramiteRegistradorDto tramiteDto);

    public void crearTramite(Tramite tramite);

    public void actualizarEstadoTramite(Tramite tramite);

    public Tramite getTramiteByCodigoValidacionTramite(String codigoValidacionTramite);

    public Boolean existenciaTramiteAsociado(Long tramiteId);

    public List<Tramite> getTramiteList(Integer institucionId, Short estado);

}
