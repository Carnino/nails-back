package jsges.nails.service.organizacion;

import java.util.ArrayList;
import jsges.nails.DTO.Organizacion.ClienteDTO;
import jsges.nails.domain.organizacion.Cliente;
import jsges.nails.repository.organizacion.ClienteRepository;
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
import jsges.nails.excepcion.RecursoNoEncontradoExcepcion;

@Service
public class ClienteService implements IClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    @Override
    public List<Cliente> listar() {
        return clienteRepository.buscarNoEliminados();
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        Cliente model = clienteRepository.findById(id).orElse(null);
        return model;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        this.validarCliente(cliente);
        return clienteRepository.save(cliente);
    }
    
    public void validarCliente(Cliente cliente){
        if (!cliente.getCelular().matches("\\d+")){
            throw new IllegalArgumentException("Numero Incorrecto.");
        }
    }

    @Override
    public void eliminar(Cliente cliente) {
          clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listar(String consulta) {
         return clienteRepository.buscarNoEliminados(consulta);
    }

    public Page<Cliente> getClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Page<ClienteDTO> findPaginated(Pageable pageable, List<ClienteDTO> clientes) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ClienteDTO> list;
        if (clientes.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, clientes.size());
            list = clientes.subList(startItem, toIndex);
        }

        Page<ClienteDTO> bookPage
                = new PageImpl<ClienteDTO>(list, PageRequest.of(currentPage, pageSize), clientes.size());

        return bookPage;
    }
    

    public List<ClienteDTO> convertirAClienteDTO(List<Cliente> clientes) {
        List<ClienteDTO> clienteDTOs = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clienteDTOs.add(new ClienteDTO(cliente)); // Asumiendo que el constructor de ClienteDTO acepta un Cliente
        }
        return clienteDTOs;
    }

    public Page<ClienteDTO> obtenerClientesPaginados(String consulta, Pageable pageable) {
        List<Cliente> clientes = listar(consulta); // Obtener los clientes según la consulta
        List<ClienteDTO> clienteDTOs = convertirAClienteDTO(clientes); // Convertir a DTO
        return findPaginated(pageable, clienteDTOs); // Paginación
    }

    public Cliente eliminarCliente(Integer id) {
    Cliente cliente = buscarPorId(id);
    if (cliente == null) throw new RecursoNoEncontradoExcepcion("Cliente no encontrado");

    cliente.setEstado(1); // Eliminar de manera lógica
    return guardar(cliente); // Guardamos el cliente con el nuevo estado
}


}
