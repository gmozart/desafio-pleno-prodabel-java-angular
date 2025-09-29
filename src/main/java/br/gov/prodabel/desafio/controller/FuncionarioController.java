package br.gov.prodabel.desafio.controller;

import br.gov.prodabel.desafio.domain.dto.FuncionarioDTO;
import br.gov.prodabel.desafio.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioDTO> criar(@RequestBody FuncionarioDTO dto) {
        FuncionarioDTO criado = funcionarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarTodos() {
        List<FuncionarioDTO> lista = funcionarioService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarPorId(@PathVariable Long id) {
        FuncionarioDTO dto = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizar(@PathVariable Long id, @RequestBody FuncionarioDTO dto) {
        FuncionarioDTO atualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
