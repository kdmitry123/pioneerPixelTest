package by.pioneerpixeltest.controller;

import by.pioneerpixeltest.PostgresTestContainer;
import by.pioneerpixeltest.security.AuthRequest;
import by.pioneerpixeltest.security.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest extends PostgresTestContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticate_WithValidCredentials_ShouldReturnToken() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setName("test1");
        request.setPassword("test");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponse.class
        );

        assertNotNull(response.getToken());
    }
}
