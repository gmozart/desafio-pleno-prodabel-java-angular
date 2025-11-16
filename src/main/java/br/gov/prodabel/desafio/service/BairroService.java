package br.gov.prodabel.desafio.service;

import br.gov.prodabel.desafio.domain.dto.BairroDTO;
import br.gov.prodabel.desafio.domain.entity.Bairro;
import br.gov.prodabel.desafio.execption.ResourceNotFoundException;
import br.gov.prodabel.desafio.repository.BairroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BairroService {

    private final BairroRepository bairroRepository;

    @Transactional
    public BairroDTO criar(BairroDTO dto) {
        // Verifica se já existe bairro com o mesmo CEP
        bairroRepository.findByCep(dto.getCep()).ifPresent(b -> {
            throw new IllegalArgumentException("Já existe um bairro cadastrado com o CEP: " + dto.getCep());
        });

        Bairro bairro = Bairro.builder()
                .nome(dto.getNome())
                .cep(dto.getCep())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .build();

        Bairro salvo = bairroRepository.save(bairro);
        return BairroDTO.of(salvo);
    }

    public List<BairroDTO> listarTodos() {
        return bairroRepository.findAll().stream()
                .map(BairroDTO::of)
                .collect(Collectors.toList());
    }

    public BairroDTO buscarPorId(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com id: " + id));
        return BairroDTO.of(bairro);
    }

    public BairroDTO buscarPorCep(String cep) {
        Bairro bairro = bairroRepository.findByCep(cep)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com CEP: " + cep));
        return BairroDTO.of(bairro);
    }

    @Transactional
    public BairroDTO atualizar(Long id, BairroDTO dto) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com id: " + id));

        // Verifica se o CEP alterado já existe em outro bairro
        if (!bairro.getCep().equals(dto.getCep())) {
            bairroRepository.findByCep(dto.getCep()).ifPresent(b -> {
                if (!b.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe um bairro cadastrado com o CEP: " + dto.getCep());
                }
            });
        }

        bairro.setNome(dto.getNome());
        bairro.setCep(dto.getCep());
        bairro.setCidade(dto.getCidade());
        bairro.setEstado(dto.getEstado());

        Bairro atualizado = bairroRepository.save(bairro);
        return BairroDTO.of(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com id: " + id));
        bairroRepository.delete(bairro);
    }
}

