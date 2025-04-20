package com.residenciatic18.apileilao.web.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residenciatic18.apileilao.services.LanceService;
import com.residenciatic18.apileilao.web.dto.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.LanceResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;

@RestController
@RequestMapping("/lance/")
public class LanceController {

  @Autowired
  private LanceService lanceService;

  @PostMapping("create")
  public ResponseEntity<?> create(@RequestBody LanceForm createDto) {
    return lanceService.createBid(createDto);
  }

  @GetMapping("{id}")
  public ResponseEntity<List<LanceResponseDto>> getById(@PathVariable Long id) {
    return lanceService.getByIdOrNotFound(id);
  }

  @GetMapping()
  public ResponseEntity<List<LanceResponseDto>> searchAll() {
    return ResponseEntity.ok(LanceMapper.toListDto(lanceService.findAll()));
  }

  @GetMapping("/leilao={id}")
  public ResponseEntity<?> getByLeilaoId(@PathVariable Long id) {
    return lanceService.getByLeilaoIdOrNotFound(id);
  }

  @GetMapping("/concorrente={id}")
  public ResponseEntity<?> getByConcorrenteId(@PathVariable Long id) {
    return lanceService.getByConcorrenteIdOrNotFound(id);
  }

  @PutMapping("{id}")
  public ResponseEntity<LanceResponseDto> update(@PathVariable Long id, @RequestBody LanceForm createDto) {
    return lanceService.updateBid(id, createDto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ConcorrenteResponseDto> delete(@PathVariable("id") Long id) {

    // Delegando a verificação e exclusão para o serviço
    Optional<ConcorrenteResponseDto> concorrenteDtoOpt = lanceService.deleteBid(id);

    if (concorrenteDtoOpt.isEmpty()) {
      return ResponseEntity.notFound().build(); // Lance não encontrado ou erro ao excluir
    }

    return ResponseEntity.ok(concorrenteDtoOpt.get()); // Retorna o DTO atualizado
  }

  @PutMapping
  public ResponseEntity<Void> handleMissingId() {
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteNoId() {
    return ResponseEntity.notFound().build();
  }

}