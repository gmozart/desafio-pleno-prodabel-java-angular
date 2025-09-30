package br.gov.prodabel.desafio.repository;

import br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.domain.entity.Funcionario;
import br.gov.prodabel.desafio.domain.entity.Solicitacao;
import br.gov.prodabel.desafio.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    boolean existsByUsuarioAndFuncionarioAndBairro(Usuario usuario, Funcionario funcionario, Bairro bairro);

    Optional<Solicitacao> findByUsuarioAndFuncionarioAndBairro(Usuario usuario, Funcionario funcionario, Bairro bairro);

    @Query("""
    SELECT new br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO(
        s.bairro.cep,
        s.bairro.nome,
        COUNT(s)
    )
    FROM Solicitacao s
    WHERE (:cep IS NULL OR s.bairro.cep = :cep)
    GROUP BY s.bairro.cep, s.bairro.nome""")
    List<AtendimentoPorBairroDTO> countAtendimentosPorBairro(
            @Param("cep") String cep
    );

}
