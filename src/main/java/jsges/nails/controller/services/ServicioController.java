package jsges.nails.controller.services;
import jsges.nails.DTO.articulos.ArticuloVentaDTO;
import jsges.nails.DTO.servicios.ItemServicioDTO;
import jsges.nails.DTO.servicios.ServicioDTO;
import jsges.nails.domain.articulos.ArticuloVenta;
import jsges.nails.domain.servicios.ItemServicio;
import jsges.nails.domain.servicios.Servicio;
import jsges.nails.domain.servicios.TipoServicio;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;
import jsges.nails.service.organizacion.IClienteService;
import jsges.nails.service.servicios.IItemServicioService;
import jsges.nails.service.servicios.IServicioService;
import jsges.nails.service.servicios.ITipoServicioService;
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
public class ServicioController {
    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);
    @Autowired
    private IServicioService modelService;
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private ITipoServicioService tipoServicioService;

    @Autowired
    private IItemServicioService itemServicioService;

    public ServicioController() {

    }
    @GetMapping({"/servicios"})
    public List<ServicioDTO> getAll() {
        return modelService.obtenerServiciosConItems();
    }


    @GetMapping("/servicio/{id}")
    public ResponseEntity<ServicioDTO> getPorId(@PathVariable Integer id) {
        logger.info("Entra en buscar servicio");
        ServicioDTO servicioDTO = modelService.obtenerServicioConItemsPorId(id);
        if (servicioDTO == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: " + id);
        }
        logger.info(servicioDTO.toString());
        return ResponseEntity.ok(servicioDTO);
    }

    @GetMapping({"/serviciosPageQuery"})
    public ResponseEntity<Page<ServicioDTO>> getItems(
            @RequestParam(defaultValue = "") String consulta, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${max_page}") int size) {

        Page<ServicioDTO> paginatedServicios = modelService.obtenerServiciosPaginados(consulta, page, size);
        return ResponseEntity.ok().body(paginatedServicios);
    }


    @PostMapping("/servicios")
    public ResponseEntity<Servicio> agregar(@RequestBody ServicioDTO modelDTO) {
        Servicio servicioGuardado = modelService.crearServicioConItems(modelDTO);
        return new ResponseEntity<>(servicioGuardado, HttpStatus.CREATED);
    }
    
    @PutMapping("/servicioEliminar/{id}")
    public ResponseEntity<Servicio> eliminarServicio(@PathVariable Integer id) {
        Servicio servicio = modelService.buscarPorId(id);
        if (servicio == null) {
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);
        }

        servicio.setEstado(1);  // Suponiendo que 1 significa "eliminado" en tu lógica
        modelService.guardar(servicio);

        return ResponseEntity.ok(servicio);
    }



    
}

