package br.gov.prodabel.desafio.controller;

import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;
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
class FuncionarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarFuncionario() throws Exception {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome("João");
        dto.setCargo(CargoFuncionario.ATENDENTE);

        mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.cargo").value("ATENDENTE"));
    }

    @Test
    void deveListarTodosFuncionarios() throws Exception {
        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {
        // Cria um funcionário primeiro
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome("Maria");
        dto.setCargo(CargoFuncionario.GERENTE);

        String json = mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        FuncionarioDTO criado = objectMapper.readValue(json, FuncionarioDTO.class);

        mockMvc.perform(get("/api/funcionarios/{id}", criado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome("Carlos");
        dto.setCargo(CargoFuncionario.SUPORTE);

        String json = mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        FuncionarioDTO criado = objectMapper.readValue(json, FuncionarioDTO.class);
        criado.setNome("Carlos Atualizado");

        mockMvc.perform(put("/api/funcionarios/{id}", criado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"));
    }

    @Test
    void deveDeletarFuncionario() throws Exception {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome("Ana");
        dto.setCargo(CargoFuncionario.ATENDENTE);

        String json = mockMvc.perform(post("/api/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        FuncionarioDTO criado = objectMapper.readValue(json, FuncionarioDTO.class);

        mockMvc.perform(delete("/api/funcionarios/{id}", criado.getId()))
                .andExpect(status().isNoContent());
    }
}
