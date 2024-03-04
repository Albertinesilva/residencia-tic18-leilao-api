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
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/lance")
public class LanceController {

  @Autowired
  private LanceService lanceService;

  @Autowired
  private ConcorrenteService concorrenteService;

  @Autowired
  private LeilaoService leilaoService;

  // @PostMapping("/create")
  // public ResponseEntity<LanceResponseDto> create(@RequestBody LanceForm
  // createDto) {
  // Lance obj = lanceService.salvar(LanceMapper.toLance(createDto));
  // URI uri =
  // ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
  // return ResponseEntity.created(uri).body(LanceMapper.toDto(obj));
  // }

  @SuppressWarnings("static-access")
  @PostMapping("/create")
  public ResponseEntity<LanceResponseDto> create(@RequestBody LanceForm createDto) {
    // Verificar se o ID do Concorrente existe
    Concorrente concorrente = concorrenteService.buscarPorId(createDto.getConcorrenteId());
    if (concorrente == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Verificar se o ID do Leilão existe e se o Leilão está aberto
    Leilao leilao = leilaoService.buscarPorId(createDto.getLeilaoId());
    if (leilao == null) {
      return ResponseEntity.badRequest().build();
    }
    if (leilao.getOrderStatus().equals(leilao.getOrderStatus().FECHADO)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Salvar o lance e retornar a resposta com o URI do recurso criado
    Lance obj = lanceService.salvar(LanceMapper.toLance(createDto));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
    return ResponseEntity.created(uri).body(LanceMapper.toDto(obj));
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<LanceResponseDto>> getById(@RequestParam(required = false) Long id) {
    List<LanceResponseDto> lance = lanceService.findById(id);

    if (!lance.isEmpty()) {
      return ResponseEntity.ok().body(lance);
    } else {
      return ResponseEntity.notFound().build();
    }
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
    try {
      return ResponseEntity.ok(LanceMapper.toDto(lanceService.update(id, createDto)));

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {

    if (lanceService.isExisteId(id)) {

      try {
        lanceService.delete(id);
        return ResponseEntity.ok().build();

      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}