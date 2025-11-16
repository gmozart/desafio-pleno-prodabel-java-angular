package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public FuncionarioDTO criar(FuncionarioDTO dto) {
        funcionarioRepository.findByEmail(dto.getEmail()).ifPresent(f -> {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        });

        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        });

        Funcionario funcionario = Funcionario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
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
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    public FuncionarioDTO atualizar(Long id, FuncionarioDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        if (!funcionario.getEmail().equals(dto.getEmail())) {
            funcionarioRepository.findByEmail(dto.getEmail()).ifPresent(f -> {
                if (!f.getId().equals(id)) {
                    throw new IllegalArgumentException("Email já cadastrado no sistema");
                }
            });

            usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
                throw new IllegalArgumentException("Email já cadastrado no sistema");
            });
        }

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCargo(dto.getCargo());

        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Funcionario atualizado = funcionarioRepository.save(funcionario);
        return FuncionarioDTO.of(atualizado);
    }

    public void deletar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        funcionarioRepository.delete(funcionario);
    }
}