package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BairroRepository bairroRepository;

    public UsuarioDTO criar(UsuarioDTO dto) {
        Bairro bairro = Optional.ofNullable(dto.getBairro().getId())
                .flatMap(bairroRepository::findById)
                .orElseGet(() ->
                        bairroRepository.findByCep(dto.getBairro().getCep())
                                .orElseGet(() -> {
                                    Bairro b = new Bairro();
                                    b.setNome(dto.getBairro().getNome());
                                    b.setCep(dto.getBairro().getCep());
                                    b.setCidade(dto.getBairro().getCidade());
                                    b.setEstado(dto.getBairro().getEstado());
                                    return bairroRepository.save(b);
                                })
                );

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
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

        Bairro bairro = Optional.ofNullable(dto.getBairro().getId())
                .flatMap(bairroRepository::findById)
                .orElseGet(() ->
                        bairroRepository.findByCep(dto.getBairro().getCep())
                                .orElseGet(() -> {
                                    Bairro b = new Bairro();
                                    b.setNome(dto.getBairro().getNome());
                                    b.setCep(dto.getBairro().getCep());
                                    b.setCidade(dto.getBairro().getCidade());
                                    b.setEstado(dto.getBairro().getEstado());
                                    return bairroRepository.save(b);
                                })
                );

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setBairro(bairro);

        return UsuarioDTO.of(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuarioRepository.deleteById(usuario.getId());
    }
}
