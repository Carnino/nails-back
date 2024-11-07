package jsges.nails.controller.services;


import jsges.nails.DTO.servicios.TipoServicioDTO;
import jsges.nails.domain.servicios.TipoServicio;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;
import jsges.nails.service.servicios.ITipoServicioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="${path_mapping}")
@CrossOrigin(value="${path_cross}")
public class TipoServicioController {
    private static final Logger logger = LoggerFactory.getLogger(TipoServicioController.class);
    @Autowired
    private ITipoServicioService modelService;

    public TipoServicioController() {

    }

    @GetMapping({"/tiposServicios"})
    public ResponseEntity<List<TipoServicio>> getAll() {
        List<TipoServicio> tipoServicios = this.modelService.listar();
        return ResponseEntity.ok(tipoServicios);
    }


    @GetMapping({"/tiposServiciosPageQuery"})
    public ResponseEntity<Page<TipoServicio>> getItems(@RequestParam(defaultValue = "") String consulta, @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "${max_page}") int size) {
        List<TipoServicio> listado = modelService.listar(consulta);
        Page<TipoServicio> bookPage = modelService.findPaginated(PageRequest.of(page, size),listado);
        return ResponseEntity.ok().body(bookPage);
    }


    @PostMapping("/tiposServicios")
    public  ResponseEntity<TipoServicio>  agregar(@RequestBody TipoServicioDTO model){
        List<TipoServicio> list = modelService.buscar(model.denominacion);
        if (!list.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
           // throw new RecursoNoEncontradoExcepcion("Ya existe una linea con la denominacion: " + model.denominacion);
        }

        TipoServicio nuevoModelo = modelService.newModel(model);
        return ResponseEntity.ok(nuevoModelo);
    }


    @PutMapping("/tipoServicioEliminar/{id}")
    public ResponseEntity<TipoServicio> eliminar(@PathVariable Integer id){
        TipoServicio model = modelService.buscarPorId(id);
        if (model == null)
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);

        model.setEstado(1);

        modelService.guardar(model);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/tiposServicios/{id}")
    public ResponseEntity<TipoServicio> getPorId(@PathVariable Integer id){
        TipoServicio cliente = modelService.buscarPorId(id);
        if(cliente == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/tiposServicios/{id}")
    public ResponseEntity<TipoServicio> actualizar(@PathVariable Integer id,
                                                   @RequestBody TipoServicio modelRecibido){
        TipoServicio model = modelService.buscarPorId(id);
        if (model == null)
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);

        modelService.guardar(modelRecibido);
        return ResponseEntity.ok(modelRecibido);
    }

}

