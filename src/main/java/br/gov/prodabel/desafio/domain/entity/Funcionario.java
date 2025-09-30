package br.gov.prodabel.desafio.domain.entity;

import br.gov.prodabel.desafio.domain.enums.CargoFuncionario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cargo não pode estar vazio")
    private CargoFuncionario cargo;

    @OneToMany(mappedBy = "funcionario")
    private List<Solicitacao> solicitacoes;

}
