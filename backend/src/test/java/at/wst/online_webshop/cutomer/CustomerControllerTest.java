package at.wst.online_webshop.cutomer;

import at.wst.online_webshop.dtos.requests.CreatingCustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testSignUp_withUnsecurePassword_ExpectWeakPasswordError() throws Exception {
        CreatingCustomerRequest request = new CreatingCustomerRequest();
        request.setName("Max Mustermann");
        request.setEmail("max8989@gmail.com");
        request.setPassword("password");
        request.setAddress("Währingerstraße 29");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting a 400 Bad Request
                .andExpect(MockMvcResultMatchers.content().string("Password is not secure"));
    }

    @Test
    public void testSignUp_withValidInput_ExpectStatusOK() throws Exception {
        CreatingCustomerRequest request = new CreatingCustomerRequest();
        request.setName("Max Mustermann");
        request.setEmail("max8989@gmail.com");
        request.setPassword("IMSEistcool23");
        request.setAddress("Währingerstraße 29");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSignUp_withInvalidEmail_ExpectBadRequest() throws Exception{
        CreatingCustomerRequest request = new CreatingCustomerRequest();
        request.setName("Max Mustermann");
        request.setEmail("amazon.com");
        request.setPassword("password");
        request.setAddress("Währingerstraße 29");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("E-mail is not valid"));
    }
}
