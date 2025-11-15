package br.gov.prodabel.desafio.controller;


import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.SolicitacaoRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SolicitacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @BeforeEach
    void limparBanco() {
        solicitacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        funcionarioRepository.deleteAll();
        bairroRepository.deleteAll();
    }




    private Bairro salvarBairro() {
        return bairroRepository.save(
                Bairro.builder()
                        .nome("Bairro Teste")
                        .cidade("Cidade T")
                        .estado("SP")
                        .cep("00000-000")
                        .build()
        );
    }

    private Usuario salvarUsuario(Bairro bairro) {
        String emailUnico = "teste" + System.currentTimeMillis() + "@gmail.com";
        return usuarioRepository.save(
                Usuario.builder()
                        .nome("Usuário Teste")
                        .email(emailUnico)
                        .bairro(bairro)
                        .senha("123456")
                        .build()
        );
    }


    private Funcionario salvarFuncionario() {
        return funcionarioRepository.save(
                Funcionario.builder()
                        .nome("Funcionario Teste")
                        .cargo(CargoFuncionario.GERENTE)
                        .email("funcionario" + System.currentTimeMillis() + "@example.com")
                        .senha("123456")
                        .build()
        );
    }

    private SolicitacaoDTO criarSolicitacaoDTO(String descricao, Usuario usuario, Funcionario funcionario, Bairro bairro) {
        return SolicitacaoDTO.builder()
                .descricao(descricao)
                .bairro(BairroDTO.builder()
                        .id(bairro.getId())
                        .nome(bairro.getNome())
                        .cidade(bairro.getCidade())
                        .estado(bairro.getEstado())
                        .cep(bairro.getCep())
                        .build())
                .status(StatusSolicitacao.ABERTA)
                .usuarioId(usuario.getId())
                .funcionarioId(funcionario.getId())
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    @Test
    void deveCriarSolicitacao() throws Exception {
        Bairro bairro = salvarBairro();
        Usuario usuario = salvarUsuario(bairro);
        Funcionario funcionario = salvarFuncionario();

        SolicitacaoDTO dto = criarSolicitacaoDTO("Teste criação", usuario, funcionario, bairro);

        mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Teste criação"))
                .andExpect(jsonPath("$.bairro.nome").value("Bairro Teste"))
                .andExpect(jsonPath("$.status").value("ABERTA"));
    }

    @Test
    void deveListarTodasSolicitacoes() throws Exception {
        // Criar dados de teste para garantir que há solicitações para listar
        Bairro bairro = salvarBairro();
        Usuario usuario = salvarUsuario(bairro);
        Funcionario funcionario = salvarFuncionario();

        SolicitacaoDTO dto = criarSolicitacaoDTO("Teste listagem", usuario, funcionario, bairro);
        mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/solicitacoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveBuscarSolicitacaoPorId() throws Exception {
        Bairro bairro = salvarBairro();
        Usuario usuario = salvarUsuario(bairro);
        Funcionario funcionario = salvarFuncionario();

        SolicitacaoDTO dto = criarSolicitacaoDTO("Teste busca", usuario, funcionario, bairro);
        String json = mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SolicitacaoDTO criado = objectMapper.readValue(json, SolicitacaoDTO.class);

        mockMvc.perform(get("/api/solicitacoes/{id}", criado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste busca"))
                .andExpect(jsonPath("$.bairro.nome").value("Bairro Teste"));
    }

    @Test
    void deveAtualizarSolicitacao() throws Exception {
        Bairro bairro = salvarBairro();
        Usuario usuario = salvarUsuario(bairro);
        Funcionario funcionario = salvarFuncionario();

        SolicitacaoDTO dto = criarSolicitacaoDTO("Teste atualização", usuario, funcionario, bairro);
        String json = mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SolicitacaoDTO criado = objectMapper.readValue(json, SolicitacaoDTO.class);
        criado.setDescricao("Atualizado");

        mockMvc.perform(put("/api/solicitacoes/{id}", criado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Atualizado"));
    }

    @Test
    void deveDeletarSolicitacao() throws Exception {
        Bairro bairro = salvarBairro();
        Usuario usuario = salvarUsuario(bairro);
        Funcionario funcionario = salvarFuncionario();

        SolicitacaoDTO dto = criarSolicitacaoDTO("Teste exclusão", usuario, funcionario, bairro);
        String json = mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        SolicitacaoDTO criado = objectMapper.readValue(json, SolicitacaoDTO.class);

        mockMvc.perform(delete("/api/solicitacoes/{id}", criado.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveObterAtendimentosPorBairro() throws Exception {
        mockMvc.perform(get("/api/solicitacoes/metricas/atendimentos-por-bairro")
                        .param("cep", "00000-000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}