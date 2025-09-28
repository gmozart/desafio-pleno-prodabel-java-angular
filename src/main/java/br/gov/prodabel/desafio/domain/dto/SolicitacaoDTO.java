package br.gov.prodabel.desafio.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private Long usuarioId;
    private Long funcionarioId;

}
