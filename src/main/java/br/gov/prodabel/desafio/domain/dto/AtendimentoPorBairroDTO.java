package br.gov.prodabel.desafio.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtendimentoPorBairroDTO{
    private String bairro;
    private Long quantidade;

    public AtendimentoPorBairroDTO(String bairro, Long quantidade) {
        this.bairro = bairro;
        this.quantidade = quantidade;
    }


}