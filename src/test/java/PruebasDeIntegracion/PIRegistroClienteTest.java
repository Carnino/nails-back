
package PruebasDeIntegracion;



import com.fasterxml.jackson.databind.ObjectMapper;
import jsges.nails.NailsApplication;
import jsges.nails.domain.organizacion.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = NailsApplication.class)
@AutoConfigureMockMvc
@Transactional
@Rollback
public class PIRegistroClienteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegistrarClienteValido() throws Exception {
        // Crea una cliente válida
        Cliente cliente = new Cliente();
        cliente.setCelular("3532402676");
        cliente.setContacto(null);
        cliente.setFechaInicio(null);
        cliente.setFechaNacimiento(null);
        cliente.setMail("mail@gmail.com");
        cliente.setLetra("martin");
        cliente.setEstado(1);
        cliente.setRazonSocial("Coser");
        

        // Realiza la solicitud POST al controlador
        mockMvc.perform(post("/nails/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.letra", is("martin")))
                .andExpect(jsonPath("$.razonSocial", is("Coser")))
                .andExpect(jsonPath("$.mail", is("mail@gmail.com")));
    }

    @Test
    public void testGuardarCategoria_NombreInvalido() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setCelular("hola");
        cliente.setContacto(null);
        cliente.setFechaInicio(null);
        cliente.setFechaNacimiento(null);
        cliente.setMail("mail@gmail.com");
        cliente.setLetra("martin");
        cliente.setEstado(1);
        cliente.setRazonSocial("Coser");

        // Realiza la solicitud POST al controlador
        mockMvc.perform(post("/nails/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest());
    }

   
}