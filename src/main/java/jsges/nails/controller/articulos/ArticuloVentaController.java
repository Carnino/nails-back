package jsges.nails.controller.articulos;

import jsges.nails.DTO.articulos.ArticuloVentaDTO;
import jsges.nails.domain.articulos.ArticuloVenta;
import jsges.nails.domain.articulos.Linea;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;
import jsges.nails.service.articulos.IArticuloVentaService;
import jsges.nails.service.articulos.ILineaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(value="${path_mapping}")
@CrossOrigin(value="${path_cross}")
public class ArticuloVentaController {
    private static final Logger logger = LoggerFactory.getLogger(ArticuloVentaController.class);
    @Autowired
    private IArticuloVentaService  modelService;

    @Autowired
    private ILineaService lineaService;

    public ArticuloVentaController() {

    }

    @GetMapping("/articulos")
    public List<ArticuloVentaDTO> getAll() {
        logger.info("Entrando en traer todos los artículos");
        return modelService.listarDTO();
    }


    @GetMapping("/articulosPageQuery")
    public ResponseEntity<Page<ArticuloVentaDTO>> getItems(
            @RequestParam(defaultValue = "") String consulta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${max_page}") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticuloVentaDTO> pageResult = modelService.listarPaginadoDTO(consulta, pageable);
        return ResponseEntity.ok().body(pageResult);
    }



    @PostMapping("/articulos")
    public ResponseEntity<ArticuloVentaDTO> agregar(@RequestBody ArticuloVentaDTO model) {
        logger.info("Intentando agregar un nuevo artículo");

        if (model.getLinea() == null) {
            throw new IllegalArgumentException("El ID de la Línea no puede ser nulo.");
        }

        Linea linea = lineaService.buscarPorId(model.getLinea());
        if (linea == null) {
            throw new IllegalArgumentException("La Línea con el ID especificado no existe.");
        }

        ArticuloVentaDTO articuloGuardado = modelService.crearArticulo(model);
        return ResponseEntity.ok(articuloGuardado);
    }




    @DeleteMapping("/articuloEliminar/{id}")
    public ResponseEntity<ArticuloVenta> eliminar(@PathVariable Integer id){
        ArticuloVenta model = modelService.buscarPorId(id);
        if (model == null){
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }

        model.asEliminado();
        modelService.guardar(model);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/articulos/{id}")
    public ResponseEntity<ArticuloVentaDTO> getPorId(@PathVariable Integer id){
        ArticuloVenta articuloVenta = modelService.buscarPorId(id);
        if(articuloVenta == null){
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        }
        ArticuloVentaDTO model = new ArticuloVentaDTO(articuloVenta);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/articulos/{id}")
    public ResponseEntity<ArticuloVentaDTO> actualizar(@PathVariable Integer id,
                                                       @RequestBody ArticuloVentaDTO modelRecibido) {
        logger.info("Actualizando artículo con ID: " + id);
        ArticuloVentaDTO updatedArticulo = modelService.actualizarArticulo(id, modelRecibido);
        return ResponseEntity.ok(updatedArticulo);
    }

}

