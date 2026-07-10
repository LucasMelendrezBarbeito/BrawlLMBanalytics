package com.brawllmbanalytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integracion de los comportamientos de seguridad (Fases 1 y 2).
 * Usa H2 en memoria via el perfil "test": no necesita MySQL ni variables de entorno.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeguridadIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    private String registrarYObtenerToken(String username) throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "username", username,
                                "email", username + "@test.com",
                                "password", "Test1234"))))
                .andExpect(status().isOk());

        String body = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "username", username,
                                "password", "Test1234"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return (String) json.readValue(body, Map.class).get("token");
    }

    @Test
    void getTierlistsEsPublico() throws Exception {
        mvc.perform(get("/tierlists")).andExpect(status().isOk());
    }

    @Test
    void crearTierlistSinTokenEsRechazado() throws Exception {
        mvc.perform(post("/tierlists/crear").param("nombre", "x"))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenInvalidoDevuelve401() throws Exception {
        mvc.perform(get("/cuentas/mias").header("Authorization", "Bearer basura"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registroConEmailInvalidoDevuelve400() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "username", "u" + System.nanoTime(),
                                "email", "no-es-email",
                                "password", "Test1234"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginCredencialesInvalidasDevuelve401() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "username", "noexiste" + System.nanoTime(),
                                "password", "x"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminImportSinAdminEsRechazado() throws Exception {
        mvc.perform(get("/admin/mapas/importar"))
                .andExpect(status().isForbidden());
    }

    @Test
    void tierlistsNoExponePasswordNiEmail() throws Exception {
        String token = registrarYObtenerToken("resp" + System.nanoTime());

        mvc.perform(post("/tierlists/crear")
                        .param("nombre", "MiTierlist")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        String body = mvc.perform(get("/tierlists"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertFalse(body.contains("\"password\""), "la respuesta no debe exponer password");
        assertFalse(body.contains("\"email\""), "la respuesta no debe exponer email");
    }
}
