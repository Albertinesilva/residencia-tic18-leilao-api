package com.residenciatic18.apileilao.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;

public interface LeilaoService {

  List<LeilaoResponseDto> searchDataByIDorAll(Long id);

  List<Leilao> findAll();

  Leilao save(Leilao leilao);

  Leilao searchById(Long id);

  Leilao update(Long id, LeilaoForm leilaoForm);

  void delete(Long id);

  boolean isExisteId(Long id);

  Optional<Leilao> winnerOfAuctionById(Long leilaoId);

  Map<String, Object> obterDadosDoVencedor(Long leilaoId);

  List<LeilaoResponseDto> findByIdOrThrow(Long id);

  Leilao updateOrThrow(Long id, LeilaoForm leilaoForm);

  void deleteOrThrow(Long id);

  ResponseEntity<Void> validateAuction(Long auctionId);
}