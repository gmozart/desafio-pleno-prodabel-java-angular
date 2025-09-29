package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SolicitacaoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private Long usuarioId;
    private Long funcionarioId;
    private BairroDTO bairro; // Agora referenciando o DTO de Bairro
    private StatusSolicitacao status;

    public static SolicitacaoDTO of(Solicitacao solicitacao) {
        return SolicitacaoDTO.builder()
                .id(solicitacao.getId())
                .descricao(solicitacao.getDescricao())
                .bairro(BairroDTO.of(solicitacao.getBairro()))
                .status(solicitacao.getStatus())
                .dataCriacao(solicitacao.getDataCriacao())
                .usuarioId(solicitacao.getUsuario() != null ? solicitacao.getUsuario().getId() : null)
                .funcionarioId(solicitacao.getFuncionario() != null ? solicitacao.getFuncionario().getId() : null)
                .build();
    }

    public static Solicitacao toEntity(SolicitacaoDTO dto, Usuario usuario, Funcionario funcionario, Bairro bairro) {
        return Solicitacao.builder()
                .descricao(dto.getDescricao())
                .bairro(bairro)
                .status(dto.getStatus())
                .dataCriacao(dto.getDataCriacao())
                .usuario(usuario)
                .funcionario(funcionario)
                .build();
    }
}

