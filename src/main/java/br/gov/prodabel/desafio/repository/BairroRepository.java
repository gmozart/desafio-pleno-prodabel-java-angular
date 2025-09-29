package br.gov.prodabel.desafio.repository;

import br.gov.prodabel.desafio.domain.entity.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, Long> {

    Optional<Bairro> findByCep(String cep);

}
