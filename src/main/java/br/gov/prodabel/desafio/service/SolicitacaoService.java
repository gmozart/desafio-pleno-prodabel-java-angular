package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO;
import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.BairroRepository;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.SolicitacaoRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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


    @Transactional
    public SolicitacaoDTO criar(SolicitacaoDTO dto) {

        Usuario usuario = buscarUsuario(dto.getUsuarioId());
        Funcionario funcionario = buscarFuncionario(dto.getFuncionarioId());
        Bairro bairro = buscarOuCriarBairro(dto.getBairro());


        checarDuplicidade(usuario, funcionario, bairro);


        Solicitacao solicitacao = SolicitacaoDTO.toEntity(dto, usuario, funcionario, bairro);
        solicitacao.setStatus(StatusSolicitacao.ABERTA);

        return SolicitacaoDTO.of(solicitacaoRepository.save(solicitacao));
    }

    @Transactional
    public SolicitacaoDTO atualizar(Long id, SolicitacaoDTO dto) {
        Solicitacao existente = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        Usuario usuario = buscarUsuario(dto.getUsuarioId());
        Funcionario funcionario = buscarFuncionario(dto.getFuncionarioId());
        Bairro bairro = buscarOuCriarBairro(dto.getBairro());

        checarDuplicidade(usuario, funcionario, bairro, id);

        existente.setDescricao(dto.getDescricao());
        existente.setUsuario(usuario);
        existente.setFuncionario(funcionario);
        existente.setBairro(bairro);
        existente.setStatus(dto.getStatus() != null ? dto.getStatus() : existente.getStatus());

        return SolicitacaoDTO.of(solicitacaoRepository.save(existente));
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
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
    }


    public List<AtendimentoPorBairroDTO> getAtendimentosPorBairro(String cep) {
        return solicitacaoRepository.countAtendimentosPorBairro(cep);
    }

    public void deletar(Long id) {
        solicitacaoRepository.deleteById(id);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private Funcionario buscarFuncionario(Long funcionarioId) {
        if (funcionarioId == null) return null;
        return funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    private Bairro buscarOuCriarBairro(BairroDTO dto) {
        return bairroRepository.findByCep(dto.getCep())
                .orElseGet(() -> bairroRepository.save(
                        Bairro.builder()
                                .cep(dto.getCep())
                                .nome(dto.getNome())
                                .cidade(dto.getCidade())
                                .estado(dto.getEstado())
                                .build()
                ));
    }



    private void checarDuplicidade(Usuario usuario, Funcionario funcionario, Bairro bairro) {
        if (solicitacaoRepository.existsByUsuarioAndFuncionarioAndBairro(usuario, funcionario, bairro)) {
            throw new IllegalStateException("Já existe uma solicitação para este usuário, bairro e funcionário.");
        }
    }

    private void checarDuplicidade(Usuario usuario, Funcionario funcionario, Bairro bairro, Long idAtual) {
        solicitacaoRepository.findByUsuarioAndFuncionarioAndBairro(usuario, funcionario, bairro)
                .ifPresent(solicitacao -> {
                    if (!solicitacao.getId().equals(idAtual)) {
                        throw new IllegalStateException("Já existe uma solicitação para este usuário, bairro e funcionário.");
                    }
                });
    }

}