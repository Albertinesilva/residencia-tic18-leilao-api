package com.residenciatic18.apileilao.web.controllers;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.web.dto.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;

@RestController
@RequestMapping("/concorrentes/")
public class ConcorrenteController {

  @Autowired
  private ConcorrenteService concorrenteService;

  @PostMapping("create")
  public ResponseEntity<ConcorrenteResponseDto> create(@RequestBody ConcorrenteForm createDto) {
    try {
      Concorrente obj = concorrenteService.save(ConcorrenteMapper.toConcorrente(createDto));
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
      return ResponseEntity.created(uri).body(ConcorrenteMapper.toDto(obj));

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<List<ConcorrenteResponseDto>> getById(@PathVariable Long id) {
    List<ConcorrenteResponseDto> concorrentes = concorrenteService.findByIdOrThrow(id);
    return ResponseEntity.ok(concorrentes);
  }

  @GetMapping
  public ResponseEntity<List<ConcorrenteResponseDto>> searchAll() {
    return ResponseEntity.ok(ConcorrenteMapper.toListDto(concorrenteService.findAll()));
  }

  @PutMapping("{id}")
  public ResponseEntity<ConcorrenteResponseDto> update(@PathVariable Long id, @RequestBody ConcorrenteForm form) {
    ConcorrenteResponseDto updated = concorrenteService.updateOrThrow(id, form);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    concorrenteService.deleteOrThrow(id);
    return ResponseEntity.ok().build();
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