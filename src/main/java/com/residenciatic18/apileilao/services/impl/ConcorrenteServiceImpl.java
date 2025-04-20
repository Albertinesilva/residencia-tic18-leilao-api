package com.residenciatic18.apileilao.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;

@Service
public class ConcorrenteServiceImpl implements ConcorrenteService {

  @Autowired
  private ConcorrenteRepository concorrenteRepository;

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public Concorrente save(Concorrente concorrente) {
    return concorrenteRepository.save(concorrente);
  }

  @Override
  @Transactional(readOnly = true)
  public Concorrente searchById(Long id) {
    return concorrenteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ConcorrenteResponseDto> searchDataByIDorAll(Long id) {

    if (id == null) {
      return ConcorrenteMapper.toListDto(findAll());
    } else {

      Concorrente concorrente = searchById(id);
      if (concorrente != null) {
        return ConcorrenteMapper.toListDto(Collections.singletonList(concorrente));

      } else {
        return new ArrayList<ConcorrenteResponseDto>();
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Concorrente> findAll() {
    return concorrenteRepository.findAll();
  }

  @Override
  @Transactional(readOnly = false)
  public Concorrente update(Long id, ConcorrenteForm concorrenteForm) {
    Concorrente concorrente = searchById(id);
    concorrente.setNome(concorrenteForm.getNome());
    concorrente.setCpf(concorrenteForm.getCpf());
    return save(concorrente);
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(Long id) {
    Concorrente concorrente = concorrenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Concorrente não encontrado"));

    // Deleta todos os lances relacionados a este concorrente
    lanceRepository.deleteByConcorrente(concorrente);

    // Agora pode excluir o concorrente
    concorrenteRepository.delete(concorrente);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ConcorrenteResponseDto> findByIdOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Competitor not found");
    }
    return searchDataByIDorAll(id);
  }

  @Override
  @Transactional(readOnly = false)
  public ConcorrenteResponseDto updateOrThrow(Long id, ConcorrenteForm form) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Competitor not found");
    }

    Concorrente updated = update(id, form);
    return ConcorrenteMapper.toDto(updated);
  }

  @Override
  @Transactional(readOnly = false)
  public void deleteOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Competitor not found");
    }
    delete(id);
  }

  @Override
  public boolean isExisteId(Long id) {
    return concorrenteRepository.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Void> validateBidder(Long bidderId) {
    // Check if the bidder exists
    if (!isExisteId(bidderId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Bidder not found
    }

    return ResponseEntity.ok().build(); // Bidder is valid
  }

}