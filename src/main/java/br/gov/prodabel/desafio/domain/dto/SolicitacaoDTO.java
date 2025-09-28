package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SolicitacaoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private Long usuarioId;
    private Long funcionarioId;
    private String bairro;
    private StatusSolicitacao status;

    public static SolicitacaoDTO of(Solicitacao solicitacao) {
        return SolicitacaoDTO.builder()
                .id(solicitacao.getId())
                .descricao(solicitacao.getDescricao())
                .bairro(solicitacao.getBairro())
                .status(solicitacao.getStatus())
                .dataCriacao(solicitacao.getDataCriacao())
                .usuarioId(solicitacao.getUsuario().getId())
                .funcionarioId(solicitacao.getFuncionario() != null ? solicitacao.getFuncionario().getId() : null)
                .build();
    }

    public static Solicitacao toEntity(SolicitacaoDTO dto, Usuario usuario, Funcionario funcionario) {
        return Solicitacao.builder()
                .descricao(dto.getDescricao())
                .bairro(dto.getBairro())
                .status(dto.getStatus())
                .dataCriacao(dto.getDataCriacao())
                .usuario(usuario)
                .funcionario(funcionario)
                .build();
    }


}
