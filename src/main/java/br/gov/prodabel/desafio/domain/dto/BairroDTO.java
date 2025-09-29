package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Bairro;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BairroDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String estado;
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
