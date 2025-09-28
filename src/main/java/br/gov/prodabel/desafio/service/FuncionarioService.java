package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioDTO criar(FuncionarioDTO dto) {
        Funcionario funcionario = Funcionario.builder()
                .nome(dto.getNome())
                .cargo(dto.getCargo())
                .build();

        Funcionario salvo = funcionarioRepository.save(funcionario);
        return FuncionarioDTO.of(salvo);
    }

    public List<FuncionarioDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(FuncionarioDTO::of)
                .collect(Collectors.toList());
    }

    public FuncionarioDTO buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .map(FuncionarioDTO::of)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public FuncionarioDTO atualizar(Long id, FuncionarioDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        funcionario.setNome(dto.getNome());
        funcionario.setCargo(dto.getCargo());

        Funcionario atualizado = funcionarioRepository.save(funcionario);
        return FuncionarioDTO.of(atualizado);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }
}