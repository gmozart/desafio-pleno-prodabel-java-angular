package br.gov.prodabel.desafio.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "bairro")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido. Formato esperado: 00000-000")
    private String cep;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;


}
