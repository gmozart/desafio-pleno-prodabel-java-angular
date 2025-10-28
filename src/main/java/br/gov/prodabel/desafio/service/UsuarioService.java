package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BairroRepository bairroRepository;
    private final PasswordEncoder passwordEncoder;
    private final FuncionarioRepository funcionarioRepository;

    public UsuarioDTO criar(UsuarioDTO dto) {
        // Validar se email já existe em Usuários
        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        });

        // ✅ Validar se email já existe em Funcionários
        funcionarioRepository.findByEmail(dto.getEmail()).ifPresent(f -> {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        });

        // Validar se o bairro foi informado
        if (dto.getBairro() == null) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }

        Bairro bairro = null;

        // Se o ID do bairro foi informado, busca pelo ID
        if (dto.getBairro().getId() != null) {
            bairro = bairroRepository.findById(dto.getBairro().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com id: " + dto.getBairro().getId()));
        }
        // Se não tem ID mas tem CEP, busca ou cria pelo CEP
        else if (dto.getBairro().getCep() != null) {
            bairro = bairroRepository.findByCep(dto.getBairro().getCep())
                    .orElseGet(() -> {
                        // Criar novo bairro
                        Bairro novoBairro = Bairro.builder()
                                .nome(dto.getBairro().getNome())
                                .cep(dto.getBairro().getCep())
                                .cidade(dto.getBairro().getCidade())
                                .estado(dto.getBairro().getEstado())
                                .build();
                        return bairroRepository.save(novoBairro);
                    });
        } else {
            throw new IllegalArgumentException("É necessário informar o ID ou o CEP do bairro");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha())) // ⚠️ Criptografa a senha
                .bairro(bairro)
                .build();

        return UsuarioDTO.of(usuarioRepository.save(usuario));
    }


    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::of)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioDTO.of(usuario);
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Validar se email já existe em outro usuário
        if (!usuario.getEmail().equals(dto.getEmail())) {
            usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new IllegalArgumentException("Email já cadastrado no sistema");
                }
            });

            // ✅ Validar se email já existe em Funcionários
            funcionarioRepository.findByEmail(dto.getEmail()).ifPresent(f -> {
                throw new IllegalArgumentException("Email já cadastrado no sistema");
            });
        }

        // Validar se o bairro foi informado
        if (dto.getBairro() == null) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }

        Bairro bairro = null;

        // Se o ID do bairro foi informado, busca pelo ID
        if (dto.getBairro().getId() != null) {
            bairro = bairroRepository.findById(dto.getBairro().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com id: " + dto.getBairro().getId()));
        }
        // Se não tem ID mas tem CEP, busca ou cria pelo CEP
        else if (dto.getBairro().getCep() != null) {
            bairro = bairroRepository.findByCep(dto.getBairro().getCep())
                    .orElseGet(() -> {
                        // Criar novo bairro
                        Bairro novoBairro = Bairro.builder()
                                .nome(dto.getBairro().getNome())
                                .cep(dto.getBairro().getCep())
                                .cidade(dto.getBairro().getCidade())
                                .estado(dto.getBairro().getEstado())
                                .build();
                        return bairroRepository.save(novoBairro);
                    });
        } else {
            throw new IllegalArgumentException("É necessário informar o ID ou o CEP do bairro");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha())); // ⚠️ Criptografa a senha
        }
        usuario.setBairro(bairro);

        return UsuarioDTO.of(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuarioRepository.deleteById(usuario.getId());
    }
}
