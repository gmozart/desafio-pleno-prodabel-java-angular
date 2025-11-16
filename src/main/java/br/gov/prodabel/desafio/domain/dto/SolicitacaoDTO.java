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
@Builder
public class SolicitacaoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private StatusSolicitacao status;

    // IDs (para compatibilidade)
    private Long usuarioId;
    private Long funcionarioId;
    private Long bairroId;

    // ✅ OBJETOS COMPLETOS
    private UsuarioDTO usuario;
    private FuncionarioDTO funcionario;
    private BairroDTO bairro;

    public static SolicitacaoDTO of(Solicitacao solicitacao) {
        SolicitacaoDTO dto = SolicitacaoDTO.builder()
                .id(solicitacao.getId())
                .descricao(solicitacao.getDescricao())
                .status(solicitacao.getStatus())
                .dataCriacao(solicitacao.getDataCriacao())
                .build();

        // Mapear Usuario
        if (solicitacao.getUsuario() != null) {
            dto.setUsuarioId(solicitacao.getUsuario().getId());
            dto.setUsuario(UsuarioDTO.of(solicitacao.getUsuario())); // ✅ OBJETO COMPLETO
        }

        // Mapear Funcionario
        if (solicitacao.getFuncionario() != null) {
            dto.setFuncionarioId(solicitacao.getFuncionario().getId());
            dto.setFuncionario(FuncionarioDTO.of(solicitacao.getFuncionario())); // ✅ OBJETO COMPLETO
        }

        // Mapear Bairro
        if (solicitacao.getBairro() != null) {
            dto.setBairroId(solicitacao.getBairro().getId());
            dto.setBairro(BairroDTO.of(solicitacao.getBairro())); // ✅ OBJETO COMPLETO
        }

        return dto;
    }

    public static Solicitacao toEntity(SolicitacaoDTO dto, Usuario usuario, Funcionario funcionario, Bairro bairro) {
        return Solicitacao.builder()
                .descricao(dto.getDescricao())
                .bairro(bairro)
                .status(dto.getStatus())
                .dataCriacao(dto.getDataCriacao() != null ? dto.getDataCriacao() : LocalDateTime.now())
                .usuario(usuario)
                .funcionario(funcionario)
                .build();
    }
}
