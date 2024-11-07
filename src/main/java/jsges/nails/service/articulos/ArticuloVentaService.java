package jsges.nails.service.articulos;

import java.util.ArrayList;
import jsges.nails.DTO.articulos.ArticuloVentaDTO;
import jsges.nails.domain.articulos.ArticuloVenta;
import jsges.nails.repository.articulos.ArticuloVentaRepository;
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
import jsges.nails.domain.articulos.Linea;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;


@Service
public class ArticuloVentaService implements IArticuloVentaService{
    @Autowired
    private ArticuloVentaRepository modelRepository;
    private static final Logger logger = LoggerFactory.getLogger(ArticuloVentaService.class);

    @Autowired 
    private LineaService lineaService;

    @Override
    public List<ArticuloVenta> listar() {
        return modelRepository.buscarNoEliminados();
    }

    @Override
    public ArticuloVenta buscarPorId(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    @Override
    public ArticuloVenta guardar(ArticuloVenta model) {
        return modelRepository.save(model);
    }

    @Override
    public void eliminar(ArticuloVenta model) {
        modelRepository.save(model);
    }

    @Override
    public List<ArticuloVenta> listar(String consulta) {
        //logger.info("service " +consulta);
        return modelRepository.buscarNoEliminados(consulta);
    }

    @Override
    public Page<ArticuloVenta> getArticulos(Pageable pageable) {
        return  modelRepository.findAll(pageable);
    }

    @Override
    public Page<ArticuloVentaDTO> findPaginated(Pageable pageable, List<ArticuloVentaDTO> listado) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ArticuloVentaDTO> list;
        if (listado.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listado.size());
            list = listado.subList(startItem, toIndex);
        }

        Page<ArticuloVentaDTO> bookPage
                = new PageImpl<ArticuloVentaDTO>(list, PageRequest.of(currentPage, pageSize), listado.size());

        return bookPage;
    }
    @Override
    public List<ArticuloVentaDTO> listarDTO() {
        List<ArticuloVenta> articulos = modelRepository.buscarNoEliminados();
        List<ArticuloVentaDTO> listadoDTO = new ArrayList<>();
        articulos.forEach(articulo -> listadoDTO.add(new ArticuloVentaDTO(articulo)));
        return listadoDTO;
    }

    @Override
    public Page<ArticuloVentaDTO> listarPaginadoDTO(String consulta, Pageable pageable) {
        List<ArticuloVenta> articulos = modelRepository.buscarNoEliminados(consulta);
        List<ArticuloVentaDTO> listadoDTO = new ArrayList<>();
        articulos.forEach(articulo -> listadoDTO.add(new ArticuloVentaDTO(articulo)));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listadoDTO.size());
        return new PageImpl<>(listadoDTO.subList(start, end), pageable, listadoDTO.size());
    }

    @Override
    public ArticuloVentaDTO crearArticulo(ArticuloVentaDTO articuloDTO) {
        ArticuloVenta articulo = new ArticuloVenta();
        articulo.setDenominacion(articuloDTO.getDenominacion());

        Linea linea = lineaService.buscarPorId(articuloDTO.getLinea());
        if (linea == null) {
            throw new IllegalArgumentException("La Línea no existe.");
        }
        articulo.setLinea(linea);

        ArticuloVenta savedArticulo = modelRepository.save(articulo);
        return new ArticuloVentaDTO(savedArticulo);
    }

    @Override
    public ArticuloVentaDTO actualizarArticulo(Integer id, ArticuloVentaDTO modelRecibido) {
        ArticuloVenta model = modelRepository.findById(id).orElseThrow(() -> 
            new RecursoNoEncontradoExcepcion("El artículo con ID " + id + " no existe."));

        Linea linea = lineaService.buscarPorId(modelRecibido.getLinea());
        if (linea == null) {
            throw new IllegalArgumentException("La línea con el ID " + modelRecibido.getLinea() + " no existe.");
        }

        model.setDenominacion(modelRecibido.getDenominacion());
        model.setLinea(linea);

        ArticuloVenta savedArticulo = modelRepository.save(model);
        return new ArticuloVentaDTO(savedArticulo);
    }


}
