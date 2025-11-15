package br.gov.prodabel.desafio.controller;

import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String gerarEmailUnico() {
        return "teste" + System.currentTimeMillis() + "@gmail.com";
    }

    @Test
    void deveCriarUsuario() throws Exception {

        BairroDTO bairro = new BairroDTO();
        bairro.setNome("Cac");
        bairro.setCidade("Belo Horizonte");
        bairro.setEstado("MG");
        bairro.setCep("30123-456");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("João Usuário");
        dto.setEmail(gerarEmailUnico());
        dto.setSenha("senhaTest123");
        dto.setBairro(bairro);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Usuário"))
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {

        BairroDTO bairro = new BairroDTO();
        bairro.setNome("Cacajau");
        bairro.setCidade("Belo Horizonte");
        bairro.setEstado("MG");
        bairro.setCep("30444-456");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Maria Usuária");
        dto.setEmail(gerarEmailUnico());
        dto.setSenha("senhaTest123");
        dto.setBairro(bairro);


        String response = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria Usuária"))
                .andExpect(jsonPath("$.email").exists())
                .andReturn().getResponse().getContentAsString();

        UsuarioDTO criado = objectMapper.readValue(response, UsuarioDTO.class);

        mockMvc.perform(get("/api/usuarios/{id}", criado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Usuária"));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {

        BairroDTO bairro = new BairroDTO();

        bairro.setNome("CPX");
        bairro.setCidade("Belo Horizonte");
        bairro.setEstado("MG");
        bairro.setCep("33344-456");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Carlos Usuário");
        dto.setEmail(gerarEmailUnico());
        dto.setSenha("senhaTest123");
        dto.setBairro(bairro);


        String response = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Carlos Usuário"))
                .andExpect(jsonPath("$.email").exists())
                .andReturn().getResponse().getContentAsString();

        UsuarioDTO criado = objectMapper.readValue(response, UsuarioDTO.class);
        criado.setNome("Carlos Atualizado");

        mockMvc.perform(put("/api/usuarios/{id}", criado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        BairroDTO bairro = new BairroDTO();

        bairro.setNome("CTX");
        bairro.setCidade("Belo Horizonte");
        bairro.setEstado("MG");
        bairro.setCep("38844-456");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Felipe Usuário");
        dto.setEmail(gerarEmailUnico());
        dto.setSenha("senhaTest123");
        dto.setBairro(bairro);


        String response = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Felipe Usuário"))
                .andExpect(jsonPath("$.email").exists())
                .andReturn().getResponse().getContentAsString();

        UsuarioDTO criado = objectMapper.readValue(response, UsuarioDTO.class);

        mockMvc.perform(delete("/api/usuarios/{id}", criado.getId()))
                .andExpect(status().isNoContent());
    }
}