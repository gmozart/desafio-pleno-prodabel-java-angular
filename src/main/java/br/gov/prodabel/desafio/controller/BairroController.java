package br.gov.prodabel.desafio.controller;

import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.service.BairroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bairros")
@RequiredArgsConstructor
@Tag(name = "Bairros", description = "Endpoints para gerenciamento de bairros")
public class BairroController {

    private final BairroService bairroService;

    @PostMapping
    @Operation(summary = "Criar novo bairro", description = "Cadastra um novo bairro no sistema")
    public ResponseEntity<BairroDTO> criar(@Valid @RequestBody BairroDTO bairroDTO) {
        BairroDTO criado = bairroService.criar(bairroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar todos os bairros", description = "Retorna a lista de todos os bairros cadastrados")
    public ResponseEntity<List<BairroDTO>> listarTodos() {
        List<BairroDTO> bairros = bairroService.listarTodos();
        return ResponseEntity.ok(bairros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar bairro por ID", description = "Retorna um bairro específico pelo seu ID")
    public ResponseEntity<BairroDTO> buscarPorId(@PathVariable Long id) {
        BairroDTO bairro = bairroService.buscarPorId(id);
        return ResponseEntity.ok(bairro);
    }

    @GetMapping("/cep/{cep}")
    @Operation(summary = "Buscar bairro por CEP", description = "Retorna um bairro específico pelo seu CEP")
    public ResponseEntity<BairroDTO> buscarPorCep(@PathVariable String cep) {
        BairroDTO bairro = bairroService.buscarPorCep(cep);
        return ResponseEntity.ok(bairro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar bairro", description = "Atualiza os dados de um bairro existente")
    public ResponseEntity<BairroDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BairroDTO bairroDTO) {
        BairroDTO atualizado = bairroService.atualizar(id, bairroDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar bairro", description = "Remove um bairro do sistema")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bairroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

