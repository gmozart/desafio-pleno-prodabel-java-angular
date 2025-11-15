package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO;
import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.SolicitacaoRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SolicitacaoServiceTest {

    private SolicitacaoRepository solicitacaoRepository;
    private UsuarioRepository usuarioRepository;
    private FuncionarioRepository funcionarioRepository;
    private BairroRepository bairroRepository;
    private SolicitacaoService service;

    @BeforeEach
    void setUp() {
        solicitacaoRepository = mock(SolicitacaoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        funcionarioRepository = mock(FuncionarioRepository.class);
        bairroRepository = mock(BairroRepository.class);
        service = new SolicitacaoService(solicitacaoRepository, usuarioRepository, funcionarioRepository, bairroRepository);
    }

    private String gerarEmailUnico() {
        return "teste" + System.currentTimeMillis() + "@gmail.com";
    }

    @Test
    void testCriarSolicitacao() {
        Bairro bairro = Bairro.builder().cep("12345").nome("Centro").cidade("Cidade").estado("UF").build();
        Usuario usuario = Usuario.builder().id(1L).bairro(bairro).build();
        Funcionario funcionario = Funcionario.builder().id(2L).build();

        BairroDTO bairroDTO = BairroDTO.builder()
                .cep("12345")
                .nome("Centro")
                .cidade("Cidade")
                .estado("UF")
                .build();

        SolicitacaoDTO dto = SolicitacaoDTO.builder()
                .descricao("Teste")
                .usuarioId(1L)
                .funcionarioId(2L)
                .bairro(bairroDTO)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(bairroRepository.findByCep("12345")).thenReturn(Optional.of(bairro));
        when(solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(usuario, funcionario, bairro)).thenReturn(false);

        Solicitacao solicitacaoSalva = Solicitacao.builder()
                .id(10L)
                .descricao("Teste")
                .usuario(usuario)
                .funcionario(funcionario)
                .bairro(bairro)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(dto.getDataCriacao())
                .build();

        when(solicitacaoRepository.save(any())).thenReturn(solicitacaoSalva);

        SolicitacaoDTO result = service.criar(dto);

        assertNotNull(result);
        assertEquals("Teste", result.getDescricao());
        assertEquals(10L, result.getId());
        assertEquals(StatusSolicitacao.ABERTA, result.getStatus());
        verify(solicitacaoRepository).save(any());
    }

    @Test
    void testBuscarPorIdNotFound() {
        when(solicitacaoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(br.gov.prodabel.desafio.execption.ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void testAtualizarSolicitacao() {

        Funcionario funcionario = Funcionario.builder().id(2L).build();
        Bairro bairro = Bairro.builder().cep("12345").nome("Centro").cidade("Cidade").estado("UF").build();
        Usuario usuario = Usuario.builder().id(1L).bairro(bairro).build();

        BairroDTO bairroDTO = BairroDTO.builder()
                .cep("12345")
                .nome("Centro")
                .cidade("Cidade")
                .estado("UF")
                .build();

        Solicitacao existente = Solicitacao.builder()
                .id(10L)
                .descricao("Antiga")
                .usuario(usuario)
                .funcionario(funcionario)
                .bairro(bairro)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();

        SolicitacaoDTO dto = SolicitacaoDTO.builder()
                .descricao("Atualizada")
                .usuarioId(1L)
                .funcionarioId(2L)
                .bairro(bairroDTO)
                .status(StatusSolicitacao.FINALIZADA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(solicitacaoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(bairroRepository.findByCep("12345")).thenReturn(Optional.of(bairro));
        when(solicitacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SolicitacaoDTO result = service.atualizar(10L, dto);

        assertNotNull(result);
        assertEquals("Atualizada", result.getDescricao());
        assertEquals(StatusSolicitacao.FINALIZADA, result.getStatus());
        verify(solicitacaoRepository).save(any());
    }

    @Test
    void testListarTodos() {


        Funcionario funcionario = Funcionario.builder()
                .nome("Funcionario Teste")
                .cargo(CargoFuncionario.GERENTE)
                .email(gerarEmailUnico())
                .senha("senhaFunc123")
                .build();

        Bairro bairro = Bairro.builder()
                .cep("12345")
                .nome("Centro")
                .cidade("Cidade")
                .estado("UF")
                .build();

        Usuario dtoUsuario = new Usuario();
        dtoUsuario.setNome("Carlos Usuário");
        dtoUsuario.setEmail(gerarEmailUnico());
        dtoUsuario.setSenha("senhaTest123");
        dtoUsuario.setBairro(bairro);

        Solicitacao solicitacaoSalva = Solicitacao.builder()
                .descricao("Teste")
                .usuario(dtoUsuario)
                .funcionario(funcionario)
                .bairro(bairro)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dtoUsuario));
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(bairroRepository.findByCep("12345")).thenReturn(Optional.of(bairro));
        when(solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(dtoUsuario, funcionario, bairro)).thenReturn(true);

        when(solicitacaoRepository.save(any())).thenReturn(solicitacaoSalva);

        when(solicitacaoRepository.findAllWithRelations()).thenReturn(List.of(solicitacaoSalva));

        List<SolicitacaoDTO> result = service.listarTodos();

        assertEquals(1, result.size());
        verify(solicitacaoRepository).findAllWithRelations();
    }

    @Test
    void testGetAtendimentosPorBairro() {
        String cep = "12345";
        AtendimentoPorBairroDTO dto = new AtendimentoPorBairroDTO();
        when(solicitacaoRepository.countAtendimentosPorBairro(cep)).thenReturn(List.of(dto));

        List<AtendimentoPorBairroDTO> result = service.getAtendimentosPorBairro(cep);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(solicitacaoRepository).countAtendimentosPorBairro(cep);
    }

    @Test
    void testDeletar() {
        Long id = 1L;
        doNothing().when(solicitacaoRepository).deleteById(id);

        service.deletar(id);

        verify(solicitacaoRepository).deleteById(id);
    }

    @Test
    void testCriarSolicitacaoNovoBairro() {
        Funcionario funcionario = Funcionario.builder().id(2L).build();

        Bairro novoBairro = Bairro.builder()
                .cep("99999")
                .nome("Novo Bairro")
                .cidade("Nova Cidade")
                .estado("NB")
                .build();

        Usuario usuario = Usuario.builder().id(1L).bairro(novoBairro).build();

        BairroDTO bairroDTO = BairroDTO.builder()
                .cep("99999")
                .nome("Novo Bairro")
                .cidade("Nova Cidade")
                .estado("NB")
                .build();

        SolicitacaoDTO dto = SolicitacaoDTO.builder()
                .descricao("Solicitação Novo Bairro")
                .usuarioId(1L)
                .funcionarioId(2L)
                .bairro(bairroDTO)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(bairroRepository.findByCep("99999")).thenReturn(Optional.empty());
        when(bairroRepository.save(any())).thenReturn(novoBairro);
        when(solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(usuario, funcionario, novoBairro)).thenReturn(false);

        Solicitacao solicitacaoSalva = Solicitacao.builder()
                .id(20L)
                .descricao("Solicitação Novo Bairro")
                .usuario(usuario)
                .funcionario(funcionario)
                .bairro(novoBairro)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(dto.getDataCriacao())
                .build();

        when(solicitacaoRepository.save(any())).thenReturn(solicitacaoSalva);

        SolicitacaoDTO result = service.criar(dto);

        assertNotNull(result);
        assertEquals("Solicitação Novo Bairro", result.getDescricao());
        assertEquals(20L, result.getId());
        assertEquals(StatusSolicitacao.ABERTA, result.getStatus());
        verify(bairroRepository).findByCep("99999");
        verify(bairroRepository).save(any());
        verify(solicitacaoRepository).save(any());
    }

    @Test
    void testCriarSolicitacaoDuplicada() {
        Usuario usuario = Usuario.builder().id(1L).build();
        Funcionario funcionario = Funcionario.builder().id(2L).build();
        Bairro bairro = Bairro.builder().cep("12345").nome("Centro").cidade("Cidade").estado("UF").build();

        BairroDTO bairroDTO = BairroDTO.builder()
                .cep("12345")
                .nome("Centro")
                .cidade("Cidade")
                .estado("UF")
                .build();

        SolicitacaoDTO dto = SolicitacaoDTO.builder()
                .descricao("Teste Duplicado")
                .usuarioId(1L)
                .funcionarioId(2L)
                .bairro(bairroDTO)
                .status(StatusSolicitacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(funcionarioRepository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(bairroRepository.findByCep("12345")).thenReturn(Optional.of(bairro));
        when(solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(usuario, funcionario, bairro)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.criar(dto));
    }
}