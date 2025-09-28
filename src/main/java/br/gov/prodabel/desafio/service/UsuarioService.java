package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDTO criar(UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioDTO.of(salvo);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::of)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioDTO::of)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        Usuario atualizado = usuarioRepository.save(usuario);
        return UsuarioDTO.of(atualizado);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}