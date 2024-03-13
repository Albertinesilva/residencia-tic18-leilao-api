package com.residenciatic18.apileilao.web.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;

@RestController
@RequestMapping("/leilao/")
public class LeilaoController {

  @Autowired
  private LeilaoService leilaoService;

  @PostMapping("create")
  public ResponseEntity<LeilaoResponseDto> create(@RequestBody LeilaoForm createDto) {
    try {
      Leilao obj = leilaoService.salvar(LeilaoMapper.toLeilao(createDto));
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
      return ResponseEntity.created(uri).body(LeilaoMapper.toDto(obj));

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<List<LeilaoResponseDto>> getById(@PathVariable Long id) {

    if (leilaoService.isExisteId(id)) {
      return ResponseEntity.ok().body(leilaoService.findById(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<LeilaoResponseDto>> buscarTodos() {
    return ResponseEntity.ok(LeilaoMapper.toListDto(leilaoService.findAll()));
  }

  @PutMapping("{id}")
  public ResponseEntity<LeilaoResponseDto> update(@PathVariable Long id, @RequestBody LeilaoForm createDto) {
    if (id == null || id <= 0) {
      return ResponseEntity.notFound().build();
    }

    if (leilaoService.isExisteId(id)) {
      return ResponseEntity.ok(LeilaoMapper.toDto(leilaoService.update(id, createDto)));

    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {

    if (leilaoService.isExisteId(id)) {
      leilaoService.delete(id);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  @SuppressWarnings("static-access")
  @GetMapping("vencedor_leilao/{id}")
  public ResponseEntity<Map<String, Object>> vencedorDoLeilao(@PathVariable Long id) {

    if (id == null || id <= 0) {
      return ResponseEntity.badRequest().build();
    }

    // Verificar se o leilão existe
    Leilao leilao = leilaoService.buscarPorId(id);
    if (leilao == null) {
      return ResponseEntity.notFound().build();
    }

    // Verificar se o leilão está fechado
    if (leilao.getLeilaoStatus().equals(leilao.getLeilaoStatus().FECHADO)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Chamar o método do repositório para buscar o leilão com o maior lance
    Optional<Leilao> optionalLeilao = leilaoService.vencedorDoLeilaoPorId(id);
    if (optionalLeilao.isEmpty()) {
      // Se não houver leilão com o ID fornecido, retornar 404 Not Found
      return ResponseEntity.notFound().build();
    }

    Leilao leilaoComMaiorLance = optionalLeilao.get();
    Double maiorLanceValor = leilaoComMaiorLance.getLances().stream()
        .mapToDouble(Lance::getValor)
        .max()
        .orElse(0.0); // Valor padrão caso não haja lances

    Concorrente concorrente = leilaoComMaiorLance.getLances().stream()
        .filter(lance -> lance.getValor().equals(maiorLanceValor))
        .findFirst()
        .map(Lance::getConcorrente)
        .orElse(null); // Retornar null se não houver concorrente

    // Criar um objeto para incluir os dados do leilão, do maior lance e do concorrente
    Map<String, Object> response = new HashMap<>();
    response.put("leilao", LeilaoMapper.toDto(leilaoComMaiorLance));
    response.put("maiorLance", maiorLanceValor);
    response.put("concorrente", concorrente != null ? ConcorrenteMapper.toDto(concorrente) : null);

    // Retornar o objeto no corpo da resposta com status 200 OK
    return ResponseEntity.ok(response);
  }

}