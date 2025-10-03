package br.gov.prodabel.desafio.domain.dto;

import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    private String nome;

    @Schema(description = "Email do usuário", example = "exemplo@email.com")
    @Email(message = "O email deve ser válido")
    private String email;

    private BairroDTO bairro;


    public static UsuarioDTO of(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .bairro(BairroDTO.of(usuario.getBairro()))
                .build();
    }

    public static Usuario toEntity(UsuarioDTO usuarioDTO, Bairro bairro){
        return Usuario.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .bairro(bairro)
                .build();
    }

}
