package jsges.nails.controller.organizacion;

import jsges.nails.DTO.Organizacion.ClienteDTO;
import jsges.nails.DTO.articulos.LineaDTO;
import jsges.nails.domain.articulos.Linea;
import jsges.nails.domain.organizacion.Cliente;
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;
import jsges.nails.service.organizacion.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value="${path_mapping}")
@CrossOrigin(value="${path_cross}")

public class ClienteControlador {
    private static final Logger logger = LoggerFactory.getLogger(ClienteControlador.class);
    @Autowired
    private IClienteService clienteServicio;


    public ClienteControlador() {
    }

    @GetMapping("/clientes")
    public List<ClienteDTO> getAll() {
        List<Cliente> clientes = clienteServicio.listar(); // Obtenemos los clientes desde el servicio
        return clienteServicio.convertirAClienteDTO(clientes); // Delegamos la conversión
    }


    @GetMapping("/clientesPageQuery")
    public ResponseEntity<Page<ClienteDTO>> getItems(
            @RequestParam(defaultValue = "") String consulta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${max_page}") int size) {

        Page<ClienteDTO> clientes = clienteServicio.obtenerClientesPaginados(consulta, PageRequest.of(page, size));
        return ResponseEntity.ok().body(clientes);
    }



    @PostMapping("/clientes")
    public Cliente agregar(@RequestBody Cliente cliente){
       // logger.info("Cliente a agregar: " + cliente);
        return clienteServicio.guardar(cliente);
    }


    @PutMapping("/clienteEliminar/{id}")
    public ResponseEntity<Cliente> eliminar(@PathVariable Integer id) {
        Cliente cliente = clienteServicio.eliminarCliente(id);
        return ResponseEntity.ok(cliente);
    }


    @GetMapping("/cliente/{id}")
    public ResponseEntity<Cliente> getPorId(@PathVariable Integer id){
        Cliente cliente = clienteServicio.buscarPorId(id);
        if(cliente == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id,
                                              @RequestBody Cliente modelRecibido){
        Cliente model = clienteServicio.buscarPorId(id);
        if (model == null)
            throw new RecursoNoEncontradoExcepcion("El id recibido no existe: " + id);



        clienteServicio.guardar(modelRecibido);
        return ResponseEntity.ok(modelRecibido);
    }

}
