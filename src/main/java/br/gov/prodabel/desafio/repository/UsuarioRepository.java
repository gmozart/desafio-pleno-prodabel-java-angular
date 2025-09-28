package br.gov.prodabel.desafio.repository;

import br.gov.prodabel.desafio.domain.dto.UsuarioDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioDTO, Long> {

}
