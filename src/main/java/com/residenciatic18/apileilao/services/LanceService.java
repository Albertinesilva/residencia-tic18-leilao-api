package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.web.dto.LanceResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;

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

}
