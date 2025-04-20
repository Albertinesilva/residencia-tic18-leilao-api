package com.residenciatic18.apileilao.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.response.LanceResponseDto;

public interface LanceService {

  List<LanceResponseDto> searchDataByIDorAll(Long id);

  List<Lance> findAll();

  List<LanceResponseDto> getByLeilaoId(Long id);

  List<LanceResponseDto> getByConcorrenteId(Long id);

  Lance save(Lance lance);

  Lance searchById(Long id);

  Lance update(Long id, LanceForm leilaoForm);

  void delete(Long id);

  Boolean isExisteId(Long id);

  Lance findHighestBidByAuctionId(Long leilaoId);

  ResponseEntity<?> createBid(LanceForm createDto);

  ResponseEntity<List<LanceResponseDto>> getByIdOrNotFound(Long id);

  ResponseEntity<List<LanceResponseDto>> getByLeilaoIdOrNotFound(Long id);

  ResponseEntity<List<LanceResponseDto>> getByConcorrenteIdOrNotFound(Long id);

  ResponseEntity<LanceResponseDto> updateBid(Long id, LanceForm createDto);

  Optional<ConcorrenteResponseDto> deleteBid(Long id);

}
