package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO;
import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import br.gov.prodabel.desafio.repository.BairroRepository;
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
    private final BairroRepository bairroRepository;


    public SolicitacaoDTO criar(SolicitacaoDTO dto) {
        Usuario usuario = buscarUsuario(dto.getUsuarioId());
        Funcionario funcionario = buscarFuncionario(dto.getFuncionarioId());
        Bairro bairro = buscarBairro(dto.getBairro().getCep());


        boolean existe = solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(
                usuario, funcionario, bairro);

        if (existe) {
            throw new IllegalStateException("Já existe uma solicitação para este usuário, bairro e funcionário.");
        }

        Solicitacao solicitacao = SolicitacaoDTO.toEntity(dto, usuario, funcionario, bairro);
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
        // Busca a solicitação pelo ID
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        // Atualiza a descrição
        solicitacao.setDescricao(dto.getDescricao());

        // Busca o bairro pelo CEP
        Bairro bairro = buscarBairro(dto.getBairro().getCep());
        solicitacao.setBairro(bairro);

        // Atualiza o usuário, se necessário
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        solicitacao.setUsuario(usuario);

        // Atualiza o funcionário, se fornecido
        if (dto.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            solicitacao.setFuncionario(funcionario);
        } else {
            solicitacao.setFuncionario(null);
        }

        // Salva as alterações
        Solicitacao atualizada = solicitacaoRepository.save(solicitacao);

        return SolicitacaoDTO.of(atualizada);
    }


    public List<AtendimentoPorBairroDTO> getAtendimentosPorBairro(String cep) {
        return solicitacaoRepository.countAtendimentosPorBairro(cep);
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

    private Bairro buscarBairro(String cep){
        if (cep == null) return null;
        return bairroRepository.findByCep(cep)
                .orElseThrow(() -> new RuntimeException("Bairro não encontrado"));
    }

}