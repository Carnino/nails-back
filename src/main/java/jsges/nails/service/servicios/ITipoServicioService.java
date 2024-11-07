package jsges.nails.service.servicios;

import jsges.nails.DTO.servicios.TipoServicioDTO;
import jsges.nails.domain.servicios.TipoServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITipoServicioService {

    public List<TipoServicio> listar();

    public TipoServicio buscarPorId(Integer id);

    public TipoServicio guardar(TipoServicio model);

    public void eliminar(TipoServicio model);

    public List<TipoServicio> listar(String consulta);

    public Page<TipoServicio> getTiposServicios(Pageable pageable);

    public Page<TipoServicio> findPaginated(Pageable pageable,List<TipoServicio> tipoServicios);


    public List<TipoServicio> buscar(String consulta);

    public TipoServicio newModel(TipoServicioDTO model);
}
