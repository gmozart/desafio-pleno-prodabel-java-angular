package br.gov.prodabel.desafio.domain.dto;

import lombok.Data;

@Data
public class AtendimentoPorBairroDTO {
    private String cep;
    private String nome;
    private Long quantidade;

    public AtendimentoPorBairroDTO() {}

    public AtendimentoPorBairroDTO(String cep, String nome, Long quantidade) {
        this.cep = cep;
        this.nome = nome;
        this.quantidade = quantidade;
    }
}
