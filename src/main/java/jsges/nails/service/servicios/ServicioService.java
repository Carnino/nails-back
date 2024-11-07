package jsges.nails.service.servicios;
import java.util.ArrayList;
import jsges.nails.DTO.articulos.ArticuloVentaDTO;
import jsges.nails.DTO.servicios.ServicioDTO;
import jsges.nails.domain.articulos.ArticuloVenta;
import jsges.nails.domain.servicios.ItemServicio;
import jsges.nails.domain.servicios.Servicio;
import jsges.nails.repository.servicios.ServicioRepository;
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
import java.util.stream.Collectors;
import jsges.nails.domain.servicios.TipoServicio;
import jsges.nails.service.organizacion.IClienteService;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private ServicioRepository modelRepository;
    @Autowired
    private IItemServicioService itemServicioService;
    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private ITipoServicioService tipoServicioService;
    private static final Logger logger = LoggerFactory.getLogger(ServicioService.class);

    @Override
    public List<Servicio> listar() {
        return modelRepository.buscarNoEliminados();
    }

    @Override
    public Servicio buscarPorId(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    @Override
    public Servicio guardar(Servicio model) {
        return modelRepository.save(model);
    }


    @Override
    public Page<Servicio> getServicios(Pageable pageable) {
        return  modelRepository.findAll(pageable);
    }



    @Override
    public Page<ServicioDTO> findPaginated(Pageable pageable, List<ServicioDTO> listado) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ServicioDTO> list;
        if (listado.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listado.size());
            list = listado.subList(startItem, toIndex);
        }

        Page<ServicioDTO> bookPage
                = new PageImpl<ServicioDTO>(list, PageRequest.of(currentPage, pageSize), listado.size());

        return bookPage;
    }

    @Override
    public List<Servicio> listar(String consulta) {
        //logger.info("service " +consulta);
        return modelRepository.buscarNoEliminados(consulta);
    }

    @Override
    public List<ServicioDTO> obtenerServiciosConItems() {
        List<Servicio> servicios = listar();
        List<ServicioDTO> listaServiciosDTO = new ArrayList<>();
        
        for (Servicio servicio : servicios) {
            // Obtener los items para cada servicio
            List<ItemServicio> items = itemServicioService.buscarPorServicio(servicio.getId());
            // Crear el DTO y agregarlo a la lista
            ServicioDTO servicioDTO = new ServicioDTO(servicio, items);
            listaServiciosDTO.add(servicioDTO);
        }

        return listaServiciosDTO;
    }
    
    @Override
    public ServicioDTO obtenerServicioConItemsPorId(Integer id) {
        Servicio servicio = modelRepository.findById(id).orElse(null);
        if (servicio == null) {
            return null;  // Se manejará en el controlador
        }
        
        // Obtener los items asociados al servicio
        List<ItemServicio> items = itemServicioService.buscarPorServicio(servicio.getId());
        
        // Crear el DTO y retornarlo
        return new ServicioDTO(servicio, items);
    }
    
    @Override
    public Page<ServicioDTO> obtenerServiciosPaginados(String consulta, int page, int size) {
        // Obtener lista de servicios según la consulta
        List<Servicio> servicios = listar(consulta);
        
        // Convertir a DTO
        List<ServicioDTO> serviciosDTO = servicios.stream()
                .map(ServicioDTO::new)
                .collect(Collectors.toList());
        
        // Paginación
        Pageable pageable = PageRequest.of(page, size);
        return findPaginated(pageable, serviciosDTO);
    }
    
    @Override
    public Servicio crearServicioConItems(ServicioDTO modelDTO) {
        // Crear el servicio
        Servicio newModel = new Servicio();
        newModel.setCliente(clienteService.buscarPorId(modelDTO.getCliente()));
        newModel.setFechaRegistro(modelDTO.getFechaDocumento());
        newModel.setFechaRealizacion(modelDTO.getFechaDocumento());
        newModel.setEstado(0);
        
        Servicio servicioGuardado = guardar(newModel);

        // Crear y guardar cada ItemServicio
        modelDTO.getListaItems().forEach(elemento -> {
            TipoServicio tipoServicio = tipoServicioService.buscarPorId(elemento.getTipoServicioId());
            ItemServicio item = new ItemServicio(
                    servicioGuardado, 
                    tipoServicio, 
                    elemento.getPrecio(), 
                    elemento.getObservaciones()
            );
            itemServicioService.guardar(item);
        });

        return servicioGuardado;
    }
}
