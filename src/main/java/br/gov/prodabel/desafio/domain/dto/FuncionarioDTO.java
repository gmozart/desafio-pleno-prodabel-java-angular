package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Funcionario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do funcionário", example = "funcionario@email.com")
    private String email;

    @Schema(description = "Senha do funcionário (mínimo 8 caracteres)")
    private String senha;

    @NotNull(message = "Cargo é obrigatório")
    @Schema(description = "Cargo do funcionário", example = "ATENDENTE")
    private CargoFuncionario cargo;

    public static FuncionarioDTO of(Funcionario funcionario){
        return FuncionarioDTO.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .email(funcionario.getEmail())
                .cargo(funcionario.getCargo())
                .build();
    }

    public static Funcionario toEntity(FuncionarioDTO funcionarioDTO){
        return Funcionario.builder()
                .id(funcionarioDTO.getId())
                .nome(funcionarioDTO.getNome())
                .email(funcionarioDTO.getEmail())
                .senha(funcionarioDTO.getSenha())
                .cargo(funcionarioDTO.getCargo())
                .build();
    }
}