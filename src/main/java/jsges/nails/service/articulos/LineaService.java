package jsges.nails.service.articulos;

import jsges.nails.DTO.articulos.LineaDTO;
import jsges.nails.domain.articulos.Linea;
import jsges.nails.repository.articulos.LineaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LineaService implements ILineaService {

    @Autowired
    private LineaRepository modelRepository;
    private static final Logger logger = LoggerFactory.getLogger(LineaService.class);

    @Override
    public List<Linea> listar() {
        return modelRepository.buscarNoEliminados();
    }

    @Override
    public Linea buscarPorId(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    @Override
    public Linea guardar(Linea model) {
        return modelRepository.save(model);
    }

    @Override
    public Linea newModel(LineaDTO modelDTO) {
        Linea model = new Linea(modelDTO);
        return guardar(model);
    }

    @Override
    public void eliminar(Linea model) {
        modelRepository.save(model);
    }

    @Override
    public List<Linea> listar(String consulta) {
        return modelRepository.buscarNoEliminados(consulta);
    }

    @Override
    public Page<Linea> getLineas(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    @Override
    public List<Linea> buscar(String consulta) {
        return modelRepository.buscarExacto(consulta);
    }

    @Override
    public Page<LineaDTO> findPaginated(Pageable pageable, List<LineaDTO> lineas) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<LineaDTO> list;
        if (lineas.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, lineas.size());
            list = lineas.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), lineas.size());
    }

    // **Método adicional**: Este método verifica si ya existe una línea antes de agregarla
    public boolean isLineaExistente(String denominacion) {
        List<Linea> lista = modelRepository.buscarExacto(denominacion);
        return !lista.isEmpty();
    }
}
