package br.gov.prodabel.desafio.service;

import static org.junit.jupiter.api.Assertions.*;

import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

class FuncionarioServiceTest {

    private FuncionarioRepository funcionarioRepository;
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        funcionarioRepository = mock(FuncionarioRepository.class);
        funcionarioService = new FuncionarioService(funcionarioRepository);
    }

    @Test
    void testCriar() {

        FuncionarioDTO dto = FuncionarioDTO.builder().nome("João").cargo(CargoFuncionario.GERENTE).build();
        Funcionario funcionario = Funcionario.builder().nome("João").cargo(CargoFuncionario.GERENTE).build();
        Funcionario salvo = Funcionario.builder().id(1L).nome("João").cargo(CargoFuncionario.GERENTE).build();
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(salvo);

        FuncionarioDTO result = funcionarioService.criar(dto);

        assertEquals("João", result.getNome());
        assertEquals("GERENTE", result.getCargo().toString());
    }

    @Test
    void testListarTodos() {
        Funcionario funcionario1 = Funcionario.builder().id(1L).nome("João").cargo(CargoFuncionario.GERENTE).build();
        Funcionario funcionario2 = Funcionario.builder().id(2L).nome("Maria").cargo(CargoFuncionario.ATENDENTE).build();

        when(funcionarioRepository.findAll()).thenReturn(Arrays.asList(funcionario1, funcionario2));

        List<FuncionarioDTO> result = funcionarioService.listarTodos();

        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
        assertEquals("Maria", result.get(1).getNome());
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Funcionario funcionario = Funcionario.builder().id(1L).nome("João").cargo(CargoFuncionario.ATENDENTE).build();
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        FuncionarioDTO result = funcionarioService.buscarPorId(1L);

        assertEquals("João", result.getNome());
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.buscarPorId(1L));
    }

    @Test
    void testAtualizar_Sucesso() {
        Funcionario funcionario = Funcionario.builder().id(1L).nome("João").cargo(CargoFuncionario.ATENDENTE).build();
        FuncionarioDTO dto =  FuncionarioDTO.builder().id(1L).nome("Maria").cargo(CargoFuncionario.GERENTE).build();
        Funcionario atualizado = Funcionario.builder().id(1L).nome("Maria").cargo(CargoFuncionario.SUPORTE).build();

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(atualizado);

        FuncionarioDTO result = funcionarioService.atualizar(1L, dto);

        assertEquals("Maria", result.getNome());
        assertEquals("SUPORTE", result.getCargo().toString());
    }

    @Test
    void testAtualizar_NaoEncontrado() {
        FuncionarioDTO dto = FuncionarioDTO.builder().id(1L).nome("Maria").cargo(CargoFuncionario.GERENTE).build();
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.atualizar(1L, dto));
    }

    @Test
    void testDeletar_Sucesso() {
        Funcionario funcionario = Funcionario.builder().id(1L).nome("João").cargo(CargoFuncionario.GERENTE).build();
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        doNothing().when(funcionarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> funcionarioService.deletar(1L));
        verify(funcionarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletar_NaoEncontrado() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.deletar(1L));
    }
}