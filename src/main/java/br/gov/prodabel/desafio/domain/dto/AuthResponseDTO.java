package br.gov.prodabel.desafio.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tipo; // "USUARIO" ou "FUNCIONARIO"
    private Long id;
    private String nome;
    private String email;
    private String cargo; // apenas para funcionários
    private BairroDTO bairro; // apenas para usuários
}

