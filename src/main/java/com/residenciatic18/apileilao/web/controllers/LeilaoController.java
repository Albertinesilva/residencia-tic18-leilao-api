package com.residenciatic18.apileilao.web.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/leilao/")
public class LeilaoController {

  @Autowired
  private LeilaoService leilaoService;

  @PostMapping("create")
  public ResponseEntity<LeilaoResponseDto> create(@RequestBody LeilaoForm createDto) {
    try {
      Leilao obj = leilaoService.save(LeilaoMapper.toLeilao(createDto));
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
      return ResponseEntity.created(uri).body(LeilaoMapper.toDto(obj));

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<List<LeilaoResponseDto>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(leilaoService.findByIdOrThrow(id));
  }

  @GetMapping
  public ResponseEntity<List<LeilaoResponseDto>> searchAll() {
    return ResponseEntity.ok(LeilaoMapper.toListDto(leilaoService.findAll()));
  }

  @PutMapping("{id}")
  public ResponseEntity<LeilaoResponseDto> update(@PathVariable Long id, @RequestBody LeilaoForm leilaoForm) {
    Leilao leilaoAtualizado = leilaoService.updateOrThrow(id, leilaoForm);
    LeilaoResponseDto dto = LeilaoMapper.toDto(leilaoAtualizado);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    try {
      leilaoService.deleteOrThrow(id);
      return ResponseEntity.noContent().build(); // 204
    } catch (ResponseStatusException e) {
      return ResponseEntity.notFound().build(); // 404
    }
  }

  @GetMapping("vencedor_leilao/{id}")
  public ResponseEntity<Map<String, Object>> getAuctionWinner(@PathVariable Long id) {
    try {
      Map<String, Object> response = leilaoService.obterDadosDoVencedor(id);
      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @PutMapping
  public ResponseEntity<Void> handleMissingId() {
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteNoId() {
    return ResponseEntity.notFound().build();
  }

  @GetMapping("vencedor_leilao/")
  public ResponseEntity<Void> AuctionWinne() {
    return ResponseEntity.badRequest().build();
  }

}