package br.gov.prodabel.desafio.controller;

import br.gov.prodabel.desafio.domain.dto.AtendimentoPorBairroDTO;
import br.gov.prodabel.desafio.domain.dto.SolicitacaoDTO;
import br.gov.prodabel.desafio.service.SolicitacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<SolicitacaoDTO> criar(@RequestBody SolicitacaoDTO dto) {
        SolicitacaoDTO criada = solicitacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoDTO>> listarTodos() {
        List<SolicitacaoDTO> lista = solicitacaoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoDTO> buscarPorId(@PathVariable Long id) {
        SolicitacaoDTO dto = solicitacaoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoDTO> atualizar(@PathVariable Long id, @RequestBody SolicitacaoDTO dto) {
        SolicitacaoDTO atualizado = solicitacaoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        solicitacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/metricas/atendimentos-por-bairro")
    public List<AtendimentoPorBairroDTO> atendimentosPorBairro(
            @RequestParam(required = false) String cep
    ) {
        return solicitacaoService.getAtendimentosPorBairro(cep);
    }

}
