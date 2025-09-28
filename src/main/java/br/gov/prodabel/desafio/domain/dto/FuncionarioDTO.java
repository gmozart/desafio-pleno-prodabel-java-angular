package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Funcionario;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String cargo;



    public static FuncionarioDTO of(Funcionario funcionario){
        return FuncionarioDTO.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cargo(funcionario.getCargo())
                .build();
    }

    public static Funcionario toEntity(FuncionarioDTO funcionarioDTO){
        return Funcionario.builder()
                .id(funcionarioDTO.getId())
                .nome(funcionarioDTO.getNome())
                .cargo(funcionarioDTO.getCargo())
                .build();

    }

}