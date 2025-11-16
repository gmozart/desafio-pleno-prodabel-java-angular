package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.AuthResponseDTO;
import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.dto.LoginRequestDTO;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import br.gov.prodabel.desafio.repository.FuncionarioRepository;
import br.gov.prodabel.desafio.repository.UsuarioRepository;
import br.gov.prodabel.desafio.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        if (usuario != null && passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            String token = jwtService.generateToken(usuario.getEmail(), "USUARIO");

            BairroDTO bairroDTO = usuario.getBairro() != null ? BairroDTO.of(usuario.getBairro()) : null;

            return AuthResponseDTO.builder()
                    .token(token)
                    .tipo("USUARIO")
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .bairro(bairroDTO)
                    .build();
        }

        Funcionario funcionario = funcionarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getSenha(), funcionario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(funcionario.getEmail(), "FUNCIONARIO");

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("FUNCIONARIO")
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .email(funcionario.getEmail())
                .cargo(funcionario.getCargo() != null ? funcionario.getCargo().name() : null)
                .build();
    }
}
