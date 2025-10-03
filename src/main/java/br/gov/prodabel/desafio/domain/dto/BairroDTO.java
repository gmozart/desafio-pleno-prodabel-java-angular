package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Bairro;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BairroDTO {

    private Long id;

    @NotBlank(message = "O nome do bairro é obrigatório")
    @Schema(description = "Nome do bairro")
    private String nome;

    @NotBlank(message = "A cidade é obrigatória")
    @Schema(description = "Cidade do bairro", example = "Recife")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    @Schema(description = "Estado do bairro", example = "Pernmabuco")
    private String estado;

    @Schema(description = "CEP do bairro", example = "00000-000")
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;

    public static BairroDTO of(Bairro bairro) {
        return BairroDTO.builder()
                .id(bairro.getId())
                .nome(bairro.getNome())
                .cidade(bairro.getCidade())
                .estado(bairro.getEstado())
                .cep(bairro.getCep())
                .build();
    }

    public static Bairro toEntity(BairroDTO bairroDTO){
        return  Bairro.builder()
                .id(bairroDTO.getId())
                .nome(bairroDTO.getNome())
                .cidade(bairroDTO.getCidade())
                .estado(bairroDTO.getEstado())
                .cep(bairroDTO.getCep())
                .build();
    }
}
