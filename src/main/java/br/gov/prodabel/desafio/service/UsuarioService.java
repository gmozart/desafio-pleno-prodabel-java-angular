package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BairroRepository bairroRepository; // adicionado

    public UsuarioDTO criar(UsuarioDTO dto) {
        Bairro bairro = bairroRepository.findById(dto.getBairro().getId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .bairro(bairro) // associa o bairro
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

        Bairro bairro = bairroRepository.findById(dto.getBairro().getId())
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setBairro(bairro); // atualiza o bairro

        Usuario atualizado = usuarioRepository.save(usuario);
        return UsuarioDTO.of(atualizado);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
