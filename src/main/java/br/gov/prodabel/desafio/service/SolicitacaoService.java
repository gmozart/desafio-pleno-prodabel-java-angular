package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.SolicitacaoRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;


    public SolicitacaoDTO criar(SolicitacaoDTO dto) {
        Usuario usuario = buscarUsuario(dto.getUsuarioId());
        Funcionario funcionario = buscarFuncionario(dto.getFuncionarioId());

        Solicitacao solicitacao = SolicitacaoDTO.toEntity(dto, usuario, funcionario);
        solicitacao.setStatus(StatusSolicitacao.A);

        Solicitacao salva = solicitacaoRepository.save(solicitacao);
        return SolicitacaoDTO.of(salva);
    }


    public List<SolicitacaoDTO> listarTodos() {
        return solicitacaoRepository.findAll()
                .stream()
                .map(SolicitacaoDTO::of)
                .collect(Collectors.toList());
    }

    public SolicitacaoDTO buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .map(SolicitacaoDTO::of)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    public SolicitacaoDTO atualizar(Long id, SolicitacaoDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setBairro(dto.getBairro());

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        solicitacao.setUsuario(usuario);

        if (dto.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            solicitacao.setFuncionario(funcionario);
        } else {
            solicitacao.setFuncionario(null);
        }

        Solicitacao atualizada = solicitacaoRepository.save(solicitacao);

        return SolicitacaoDTO.of(atualizada);
    }

    public void deletar(Long id) {
        solicitacaoRepository.deleteById(id);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private Funcionario buscarFuncionario(Long funcionarioId) {
        if (funcionarioId == null) return null;
        return funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

}