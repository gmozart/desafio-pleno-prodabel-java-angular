package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Funcionario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FuncionarioDTO {
    private Long id;

    @NotBlank(message = "O nome do funcionário é obrigatório")
    @Schema(description = "Nome do funcionário")
    private String nome;

    @Schema(description = "Cargo do funcionário", example = "ATENDENTE," + " SUPORTE," + " GERENTE")
    private CargoFuncionario cargo;

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