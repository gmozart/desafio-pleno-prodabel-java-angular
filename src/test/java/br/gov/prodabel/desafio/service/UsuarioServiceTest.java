package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private BairroRepository bairroRepository;
    private UsuarioService usuarioService;


    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        bairroRepository = mock(BairroRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        FuncionarioRepository funcionarioRepository = mock(FuncionarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository, bairroRepository, passwordEncoder, funcionarioRepository);
    }

    @Test
    void testCriarComBairroExistentePorId() {
        Bairro bairro = new Bairro();
        bairro.setId(1L);
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setId(1L);
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Teste");
        dto.setEmail("teste@email.com");
        dto.setBairro(bairroDTO);

        when(bairroRepository.findById(1L)).thenReturn(Optional.of(bairro));
        Usuario usuarioSalvo = Usuario.builder().nome("Teste").email("teste@email.com").bairro(bairro).build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioDTO result = usuarioService.criar(dto);

        assertEquals("Teste", result.getNome());
        verify(bairroRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testCriarComBairroExistentePorCep() {
        Bairro bairro = new Bairro();
        bairro.setId(2L);
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setCep("12345");
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Teste2");
        dto.setEmail("teste2@email.com");
        dto.setBairro(bairroDTO);

        when(bairroRepository.findById(null)).thenReturn(Optional.empty());
        when(bairroRepository.findByCep("12345")).thenReturn(Optional.of(bairro));
        Usuario usuarioSalvo = Usuario.builder().nome("Teste2").email("teste2@email.com").bairro(bairro).build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioDTO result = usuarioService.criar(dto);

        assertEquals("Teste2", result.getNome());
        verify(bairroRepository).findByCep("12345");
    }

    @Test
    void testCriarComNovoBairro() {
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setCep("99999");
        bairroDTO.setNome("Novo Bairro");
        bairroDTO.setCidade("Cidade");
        bairroDTO.setEstado("Estado");
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Novo");
        dto.setEmail("novo@email.com");
        dto.setBairro(bairroDTO);

        when(bairroRepository.findById(null)).thenReturn(Optional.empty());
        when(bairroRepository.findByCep("99999")).thenReturn(Optional.empty());
        Bairro bairroSalvo = new Bairro();
        bairroSalvo.setId(3L);
        when(bairroRepository.save(any(Bairro.class))).thenReturn(bairroSalvo);
        Usuario usuarioSalvo = Usuario.builder().nome("Novo").email("novo@email.com").bairro(bairroSalvo).build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioDTO result = usuarioService.criar(dto);

        assertEquals("Novo", result.getNome());
        verify(bairroRepository).save(any(Bairro.class));
    }

    @Test
    void testListarTodos() {
        Bairro bairro = new Bairro();
        Usuario usuario = Usuario.builder().nome("A").email("a@email.com").bairro(bairro).build();
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDTO> result = usuarioService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getNome());
    }

    @Test
    void testBuscarPorIdEncontrado() {
        Bairro bairro = new Bairro();
        Usuario usuario = Usuario.builder().nome("B").email("b@email.com").bairro(bairro).build();
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));

        UsuarioDTO result = usuarioService.buscarPorId(10L);

        assertEquals("B", result.getNome());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void testAtualizarComBairroExistentePorId() {
        Bairro bairro = new Bairro();
        bairro.setId(1L);
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setId(1L);
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado");
        dto.setEmail("atualizado@email.com");
        dto.setBairro(bairroDTO);

        Usuario usuario = Usuario.builder().nome("Antigo").email("antigo@email.com").bairro(bairro).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(bairroRepository.findById(1L)).thenReturn(Optional.of(bairro));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = usuarioService.atualizar(1L, dto);

        assertEquals("Atualizado", result.getNome());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testAtualizarComBairroExistentePorCep() {
        Bairro bairro = new Bairro();
        bairro.setId(2L);
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setCep("22222");
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado2");
        dto.setEmail("atualizado2@email.com");
        dto.setBairro(bairroDTO);

        Usuario usuario = Usuario.builder().nome("Antigo2").email("antigo2@email.com").bairro(bairro).build();
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(bairroRepository.findById(null)).thenReturn(Optional.empty());
        when(bairroRepository.findByCep("22222")).thenReturn(Optional.of(bairro));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = usuarioService.atualizar(2L, dto);

        assertEquals("Atualizado2", result.getNome());
        verify(bairroRepository).findByCep("22222");
    }

    @Test
    void testAtualizarComNovoBairro() {
        BairroDTO bairroDTO = new BairroDTO();
        bairroDTO.setCep("33333");
        bairroDTO.setNome("Novo Bairro");
        bairroDTO.setCidade("Cidade");
        bairroDTO.setEstado("Estado");
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado3");
        dto.setEmail("atualizado3@email.com");
        dto.setBairro(bairroDTO);

        Usuario usuario = Usuario.builder().nome("Antigo3").email("antigo3@email.com").bairro(new Bairro()).build();
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(bairroRepository.findById(null)).thenReturn(Optional.empty());
        when(bairroRepository.findByCep("33333")).thenReturn(Optional.empty());
        Bairro bairroSalvo = new Bairro();
        bairroSalvo.setId(3L);
        when(bairroRepository.save(any(Bairro.class))).thenReturn(bairroSalvo);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = usuarioService.atualizar(3L, dto);

        assertEquals("Atualizado3", result.getNome());
        verify(bairroRepository).save(any(Bairro.class));
    }

    @Test
    void testAtualizarUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        UsuarioDTO dto = new UsuarioDTO();

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.atualizar(99L, dto));
    }

    @Test
    void testDeletarUsuarioExistente() {
        Usuario usuario = Usuario.builder().id(5L).nome("Del").email("del@email.com").build();
        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(5L);

        verify(usuarioRepository).deleteById(5L);
    }

    @Test
    void testDeletarUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(88L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(88L));
    }
}