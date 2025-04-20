package com.residenciatic18.apileilao.services.impl;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.services.LanceService;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.response.LanceResponseDto;

@Service
@Transactional(readOnly = false)
public class LanceServiceImpl implements LanceService {

  @Autowired
  private LanceRepository lanceRepository;

  @Autowired
  private LeilaoService leilaoService;

  @Autowired
  private ConcorrenteService concorrenteService;

  @Override
  @Transactional
  public Lance save(Lance lance) {
    return lanceRepository.save(lance);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> searchDataByIDorAll(Long id) {

    if (id == null) {
      return LanceMapper.toListDto(findAll());

    } else {

      Lance lance = searchById(id);
      if (lance != null) {
        return LanceMapper.toListDto(Collections.singletonList(lance));

      } else {
        return Collections.emptyList();
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByLeilaoId(Long id) {
    Leilao leilao = new Leilao();
    leilao.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByLeilaoId(leilao.getId()));
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByConcorrenteId(Long id) {
    Concorrente concorrente = new Concorrente();
    concorrente.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByConcorrenteId(concorrente.getId()));
  }

  @Override
  public List<Lance> findAll() {
    return lanceRepository.findAll();
  }

  @Override
  public Lance searchById(Long id) {
    return lanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o lance:" + id));
  }

  @Override
  public Lance update(Long id, LanceForm lanceForm) {

    Lance lance = searchById(id);

    // Atualizar o leilão e o concorrente do lance
    Leilao leilao = leilaoService.searchById(lanceForm.getLeilaoId());
    if (leilao == null) {
      throw new NoSuchElementException("Leilão com ID " + lanceForm.getLeilaoId() + " não encontrado");
    }
    lance.setLeilao(leilao);

    Concorrente concorrente = concorrenteService.searchById(lanceForm.getConcorrenteId());
    if (concorrente == null) {
      throw new NoSuchElementException("Concorrente com ID " + lanceForm.getConcorrenteId() + " não encontrado");
    }
    lance.setConcorrente(concorrente);

    Double valor = lanceForm.getValor();
    if (valor == null || valor <= 0) {
      throw new IllegalArgumentException("O valor do lance deve ser maior que zero");
    }
    lance.setValor(valor);

    return save(lance);
  }

  @Override
  public void delete(Long id) {
    lanceRepository.deleteById(id);
  }

  @Override
  public Boolean isExisteId(Long id) {
    if (lanceRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Lance findHighestBidByAuctionId(Long leilaoId) {
    Lance lance = searchById(leilaoId);

    if (lance.getValor() == lanceRepository.findAll().stream().mapToDouble(Lance::getValor).max().getAsDouble()) {
      return lance;
    } else {
      return null;
    }
  }

  @Override
  @Transactional
  public ResponseEntity<?> createBid(LanceForm createDto) {
    // Validate auction
    ResponseEntity<Void> auctionValidation = leilaoService.validateAuction(createDto.getLeilaoId());
    if (!auctionValidation.getStatusCode().is2xxSuccessful()) {
      return auctionValidation;
    }

    // Validate bidder
    ResponseEntity<Void> bidderValidation = concorrenteService.validateBidder(createDto.getConcorrenteId());
    if (!bidderValidation.getStatusCode().is2xxSuccessful()) {
      return bidderValidation;
    }

    // Create and save the bid
    Lance lance = lanceRepository.save(LanceMapper.toLance(createDto));

    // Build URI
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(lance.getId())
        .toUri();

    return ResponseEntity.created(uri).body(LanceMapper.toDto(lance));
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByIdOrNotFound(Long id) {
    if (!isExisteId(id)) {
      return ResponseEntity.notFound().build();
    }
    List<LanceResponseDto> response = searchDataByIDorAll(id);
    return ResponseEntity.ok(response);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByLeilaoIdOrNotFound(Long id) {
    List<LanceResponseDto> lances = getByLeilaoId(id);
    if (lances.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(lances);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByConcorrenteIdOrNotFound(Long id) {
    List<LanceResponseDto> lances = getByConcorrenteId(id);
    if (lances.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(lances);
  }

  @Override
  @Transactional(readOnly = false)
  public ResponseEntity<LanceResponseDto> updateBid(Long id, LanceForm createDto) {
    // Verificar se o leilão existe
    if (!leilaoService.isExisteId(createDto.getLeilaoId())) {
      return ResponseEntity.badRequest().build(); // Leilão não encontrado
    }

    // Verificar se o leilão está fechado
    Leilao leilao = leilaoService.searchById(createDto.getLeilaoId());
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Leilão fechado
    }

    // Verificar se o concorrente existe
    if (!concorrenteService.isExisteId(createDto.getConcorrenteId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Concorrente não encontrado
    }

    // Verificar se o lance existe (id válido)
    Optional<Lance> existingLanceOpt = lanceRepository.findById(id);
    if (!existingLanceOpt.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Lance não encontrado (ID inválido)
    }

    // Atualizar o lance utilizando o método update do LanceServiceImpl
    Lance updatedLance = update(id, createDto); // Chama o método update do LanceServiceImpl

    // Retornar o lance atualizado
    return ResponseEntity.ok(LanceMapper.toDto(updatedLance));
  }

  @Override
  @Transactional(readOnly = false)
  public Optional<ConcorrenteResponseDto> deleteBid(Long id) {
    // Verifica se o lance existe
    Optional<Lance> lanceOpt = lanceRepository.findById(id);
    if (lanceOpt.isEmpty()) {
      return Optional.empty(); // Lance não encontrado
    }

    Lance lance = lanceOpt.get();

    // Verifica se o leilão associado ao lance está fechado
    Leilao leilao = leilaoService.searchById(lance.getLeilao().getId());
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return Optional.empty(); // Leilão fechado, não pode excluir o lance
    }

    // Exclui o lance
    lanceRepository.delete(lance);

    // Retorna o DTO atualizado do concorrente
    ConcorrenteResponseDto concorrenteDto = ConcorrenteMapper.toDto(lance.getConcorrente());
    return Optional.of(concorrenteDto); // Retorna o DTO com o concorrente atualizado
  }

}