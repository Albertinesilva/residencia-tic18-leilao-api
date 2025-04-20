package com.residenciatic18.apileilao.services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;
import com.residenciatic18.apileilao.web.dto.response.LeilaoResponseDto;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = false)
public class LeilaoServiceImpl implements LeilaoService {

  @Autowired
  private LeilaoRepository leilaoRepository;

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public Leilao save(Leilao leilao) {
    return leilaoRepository.save(leilao);
  }

  @Override
  @Transactional(readOnly = true)
  public Leilao searchById(Long id) {
    return leilaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> searchDataByIDorAll(Long id) {

    if (id == null) {
      return LeilaoMapper.toListDto(leilaoRepository.findAll());

    } else {

      Leilao leilao = searchById(id);
      if (leilao != null) {
        return LeilaoMapper.toListDto(Collections.singletonList(leilao));

      } else {
        return Collections.emptyList();
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Leilao> findAll() {
    return leilaoRepository.findAll();
  }

  @Override
  public Leilao update(Long id, LeilaoForm leilaoForm) {
    Leilao leilao = searchById(id);
    leilao.setDescricao(leilaoForm.getDescricao());
    leilao.setValorMinimo(leilaoForm.getValorMinimo());
    leilao.setLeilaoStatus(leilaoForm.getLeilaoStatus());
    return save(leilao);
  }

  @Override
  public void delete(Long id) {
    // Verificar se o leilão existe
    Leilao leilao = leilaoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

    // Se não estiver usando cascata, você pode excluir os lances manualmente
    lanceRepository.deleteByLeilao(leilao);

    // Agora excluir o leilão
    leilaoRepository.delete(leilao);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Object> obterDadosDoVencedor(Long leilaoId) {
    if (!isExisteId(leilaoId)) {
      throw new EntityNotFoundException("Leilão não encontrado");
    }

    Leilao leilao = searchById(leilaoId);

    if (LeilaoStatus.FECHADO.equals(leilao.getLeilaoStatus())) {
      throw new IllegalStateException("Leilão está fechado");
    }

    Optional<Leilao> optionalLeilao = winnerOfAuctionById(leilaoId);
    if (optionalLeilao.isEmpty()) {
      throw new EntityNotFoundException("Leilão sem lances");
    }

    Leilao leilaoComMaiorLance = optionalLeilao.get();
    Double maiorLanceValor = leilaoComMaiorLance.getLances().stream()
        .mapToDouble(Lance::getValor)
        .max()
        .orElse(0.0);

    Concorrente concorrente = leilaoComMaiorLance.getLances().stream()
        .filter(lance -> lance.getValor().equals(maiorLanceValor))
        .findFirst()
        .map(Lance::getConcorrente)
        .orElse(null);

    Map<String, Object> response = new HashMap<>();
    response.put("leilao", LeilaoMapper.toDto(leilaoComMaiorLance));
    response.put("maiorLance", maiorLanceValor);
    response.put("concorrente", concorrente != null ? ConcorrenteMapper.toDto(concorrente) : null);

    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Leilao> winnerOfAuctionById(Long leilaoId) {
    return leilaoRepository.findLeilaoWithMaiorLanceAndConcorrenteById(leilaoId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> findByIdOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    return searchDataByIDorAll(id);
  }

  @Override
  @Transactional(readOnly = false)
  public Leilao updateOrThrow(Long id, LeilaoForm leilaoForm) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    return update(id, leilaoForm); // assuming update() performs the update
  }

  @Override
  @Transactional(readOnly = false)
  public void deleteOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    delete(id); // assuming delete() performs the actual deletion
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Void> validateAuction(Long auctionId) {
    // Check if the auction exists
    if (!isExisteId(auctionId)) {
      return ResponseEntity.badRequest().build(); // Auction not found
    }

    // Check if the auction is closed
    Leilao leilao = searchById(auctionId);
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Auction is closed
    }

    return ResponseEntity.ok().build(); // Auction is valid
  }

  @Override
  public boolean isExisteId(Long id) {
    return leilaoRepository.existsById(id);
  }

}