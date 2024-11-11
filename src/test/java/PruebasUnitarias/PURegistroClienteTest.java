
package PruebasUnitarias;



import jsges.nails.domain.organizacion.Cliente;
import jsges.nails.repository.organizacion.ClienteRepository;
import jsges.nails.service.organizacion.ClienteService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PURegistroClienteTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setLetra("Test");
        cliente.setCelular("3532417388");
        cliente.setMail("cerquattiulises@gmail.com");
        String mail = "cerquattiulises@gmail.com";
        
         // Simulamos el comportamiento del repositorio
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        Cliente clienteGuardado = clienteService.guardar(cliente);

        assertNotNull(clienteGuardado);
        assertEquals(mail, clienteGuardado.getMail());

        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    public void testGuardarClienteInvalido() {
        Cliente cliente = new Cliente();
        cliente.setLetra("Test");
        cliente.setCelular("353hola");
        cliente.setMail("cerquattiulises@gmail.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.guardar(cliente);
        });

        assertEquals("Numero Incorrecto.", exception.getMessage());
    }
}

