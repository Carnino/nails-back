package jsges.nails.service.articulos;

import jsges.nails.DTO.articulos.LineaDTO;
import jsges.nails.domain.articulos.ArticuloVenta;
import jsges.nails.domain.articulos.Linea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ILineaService {

    public List<Linea> listar();

    public Linea buscarPorId(Integer id);

    public Linea guardar(Linea model);

    public void eliminar(Linea model);

    public List<Linea> listar(String consulta);
    public Page<Linea> getLineas(Pageable pageable);

    public Page<LineaDTO> findPaginated(Pageable pageable,List<LineaDTO> lineas);


    public List<Linea> buscar(String consulta);

    public Linea newModel(LineaDTO model);

    public boolean isLineaExistente(String denominacion);
}
