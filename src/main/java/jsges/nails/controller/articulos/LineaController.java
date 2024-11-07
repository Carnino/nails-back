package jsges.nails.controller.articulos;

import jsges.nails.DTO.articulos.LineaDTO;
import jsges.nails.domain.articulos.Linea;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;
import jsges.nails.service.articulos.ILineaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="${path_mapping}")
@CrossOrigin(value="${path_cross}")
public class LineaController {
    private static final Logger logger = LoggerFactory.getLogger(LineaController.class);

    @Autowired
    private ILineaService modelService;

    @GetMapping({"/lineas"})
    public List<LineaDTO> getAll() {
        logger.info("entra en traer todas las lineas");
        List<Linea> list = modelService.listar();
        List<LineaDTO> listadoDTO = new ArrayList<>();
        list.forEach((model) -> listadoDTO.add(new LineaDTO(model)));
        return listadoDTO;
    }

    @GetMapping({"/lineasPageQuery"})
    public ResponseEntity<Page<LineaDTO>> getItems(@RequestParam(defaultValue = "") String consulta,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "${max_page}") int size) {
        List<LineaDTO> listadoDTO = new ArrayList<>();
        List<Linea> listado = modelService.listar(consulta);
        listado.forEach((model) -> listadoDTO.add(new LineaDTO(model)));
        Page<LineaDTO> bookPage = modelService.findPaginated(PageRequest.of(page, size), listadoDTO);
        return ResponseEntity.ok().body(bookPage);
    }

    @PostMapping("/linea")
    public ResponseEntity<Linea> agregar(@RequestBody LineaDTO model) {
        if (modelService.isLineaExistente(model.denominacion)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Linea nuevaLinea = modelService.newModel(model);
        return ResponseEntity.ok(nuevaLinea);
    }

    @PutMapping("/lineaEliminar/{id}")
    public ResponseEntity<Linea> eliminar(@PathVariable Integer id) {
        Linea model = modelService.buscarPorId(id);
        if (model == null) {
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }
        model.asEliminado();
        modelService.guardar(model);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/linea/{id}")
    public ResponseEntity<LineaDTO> getPorId(@PathVariable Integer id) {
        Linea linea = modelService.buscarPorId(id);
        if (linea == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: " + id);
        }
        LineaDTO model = new LineaDTO(linea);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/linea/{id}")
    public ResponseEntity<Linea> actualizar(@PathVariable Integer id, @RequestBody LineaDTO modelRecibido) {
        Linea model = modelService.buscarPorId(id);
        if (model == null) {
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }
        model.setDenominacion(modelRecibido.denominacion);
        modelService.guardar(model);
        return ResponseEntity.ok(model);
    }
}
