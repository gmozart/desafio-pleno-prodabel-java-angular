package br.gov.prodabel.desafio.domain.entity;

import br.gov.prodabel.desafio.domain.enums.StatusSolicitacao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "solicitacao")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição não pode estar vazio")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusSolicitacao status;


    @NotNull(message = "Data de criação não pode estar vazio")
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "bairro_id")
    private Bairro bairro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}