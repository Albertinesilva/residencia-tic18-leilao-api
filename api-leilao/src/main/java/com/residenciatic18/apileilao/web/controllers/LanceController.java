package com.residenciatic18.apileilao.web.controllers;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.services.LanceService;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.LanceResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;

@RestController
@RequestMapping("/lance/")
public class LanceController {

  @Autowired
  private LanceService lanceService;

  @Autowired
  private ConcorrenteService concorrenteService;

  @Autowired
  private LeilaoService leilaoService;

  // @SuppressWarnings("static-access")
  @PostMapping("create")
  public ResponseEntity<LanceResponseDto> create(@RequestBody LanceForm createDto) {
    // Verificar se o leilão existe
    if (!leilaoService.isExisteId(createDto.getLeilaoId())) {
      return ResponseEntity.badRequest().build();
    }

    // Verificar se o leilão está fechado
    Leilao leilao = leilaoService.buscarPorId(createDto.getLeilaoId());
    if (leilao.getLeilaoStatus().equals(leilao.getLeilaoStatus().FECHADO)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Verificar se o concorrente existe
    if (!concorrenteService.isExisteId(createDto.getConcorrenteId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Salvar o lance
    Lance obj = lanceService.salvar(LanceMapper.toLance(createDto));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
    return ResponseEntity.created(uri).body(LanceMapper.toDto(obj));
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<LanceResponseDto>> getById(@PathVariable Long id) {
    if (lanceService.isExisteId(id)) {
      return ResponseEntity.ok().body(lanceService.findById(id));
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/")
  public ResponseEntity<List<LanceResponseDto>> buscarTodos() {
    return ResponseEntity.ok(LanceMapper.toListDto(lanceService.findAll()));
  }

  @GetMapping("/leilao={id}")
  public ResponseEntity<?> getByLeilaoId(@PathVariable Long id) {

    List<LanceResponseDto> lances = lanceService.getByLeilaoId(id);

    if (!lances.isEmpty()) {
      return ResponseEntity.ok(lances);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/concorrente={id}")
  public ResponseEntity<?> getByConcorrenteId(@PathVariable Long id) {
    List<LanceResponseDto> lances = lanceService.getByConcorrenteId(id);
    if (!lances.isEmpty()) {
      return ResponseEntity.ok(lances);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<LanceResponseDto> update(@PathVariable Long id, @RequestBody LanceForm createDto) {

    // Verificar se o leilão existe
    if (!leilaoService.isExisteId(createDto.getLeilaoId())) {
      return ResponseEntity.badRequest().build();
    }

    // Verificar se o leilão está fechado
    Leilao leilao = leilaoService.buscarPorId(createDto.getLeilaoId());
    if (leilao.getLeilaoStatus().equals(leilao.getLeilaoStatus().FECHADO)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Verificar se o concorrente existe
    if (!concorrenteService.isExisteId(createDto.getConcorrenteId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return ResponseEntity.ok(LanceMapper.toDto(lanceService.update(id, createDto)));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {

    if (id == null || id <= 0) {
      return ResponseEntity.notFound().build();
    }

    if (!concorrenteService.isExisteId(id)) {
      return ResponseEntity.notFound().build();
    }

    Concorrente concorrente = concorrenteService.buscarPorId(id);
    if (concorrente == null) {
      return ResponseEntity.notFound().build();
    }

    Leilao leilao = leilaoService.buscarPorId(id);
    if (leilao.getLeilaoStatus().equals(leilao.getLeilaoStatus().FECHADO)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Excluir o lance
    lanceService.delete(id);
    return ResponseEntity.ok().build();
  }

}