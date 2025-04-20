package com.residenciatic18.apileilao.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;

public interface ConcorrenteService {

  Concorrente save(Concorrente aeroporto);

  List<ConcorrenteResponseDto> searchDataByIDorAll(Long id);

  List<Concorrente> findAll();

  Concorrente searchById(Long id);

  Concorrente update(Long id, ConcorrenteForm aeroportoForm);

  void delete(Long id);

  boolean isExisteId(Long id);

  List<ConcorrenteResponseDto> findByIdOrThrow(Long id);

  ConcorrenteResponseDto updateOrThrow(Long id, ConcorrenteForm form);

  void deleteOrThrow(Long id);

  ResponseEntity<Void> validateBidder(Long bidderId);
}
