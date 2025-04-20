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
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;

/**
 * Controlador de operações relacionadas aos concorrentes.
 * 
 * A classe {@link ConcorrenteController} gerencia as requisições HTTP relacionadas ao 
 * gerenciamento de concorrentes. Ela oferece endpoints para criar, ler, atualizar e deletar 
 * informações de concorrentes, utilizando o serviço {@link ConcorrenteService} para a lógica de negócios. 
 * Os dados são transferidos entre a API e o cliente usando DTOs ({@link ConcorrenteForm} e {@link ConcorrenteResponseDto}).
 */
@RestController
@RequestMapping("/concorrentes/")
public class ConcorrenteController {

  @Autowired
  private ConcorrenteService concorrenteService;

  /**
   * Cria um novo concorrente.
   * 
   * @param createDto os dados do novo concorrente no formato de DTO.
   * @return a resposta HTTP com o status 201 (Created) e o objeto {@link ConcorrenteResponseDto} do concorrente criado.
   */
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

  /**
   * Busca um concorrente pelo seu ID.
   * 
   * @param id o ID do concorrente a ser buscado.
   * @return a resposta HTTP com o status 200 (OK) e a lista de {@link ConcorrenteResponseDto} correspondente.
   */
  @GetMapping("{id}")
  public ResponseEntity<List<ConcorrenteResponseDto>> getById(@PathVariable Long id) {
    List<ConcorrenteResponseDto> concorrentes = concorrenteService.findByIdOrThrow(id);
    return ResponseEntity.ok(concorrentes);
  }

  /**
   * Retorna todos os concorrentes cadastrados.
   * 
   * @return a resposta HTTP com o status 200 (OK) e a lista de {@link ConcorrenteResponseDto}.
   */
  @GetMapping
  public ResponseEntity<List<ConcorrenteResponseDto>> searchAll() {
    return ResponseEntity.ok(ConcorrenteMapper.toListDto(concorrenteService.findAll()));
  }

  /**
   * Atualiza um concorrente existente.
   * 
   * @param id o ID do concorrente a ser atualizado.
   * @param form os dados de atualização no formato de DTO {@link ConcorrenteForm}.
   * @return a resposta HTTP com o status 200 (OK) e o objeto {@link ConcorrenteResponseDto} do concorrente atualizado.
   */
  @PutMapping("{id}")
  public ResponseEntity<ConcorrenteResponseDto> update(@PathVariable Long id, @RequestBody ConcorrenteForm form) {
    ConcorrenteResponseDto updated = concorrenteService.updateOrThrow(id, form);
    return ResponseEntity.ok(updated);
  }

  /**
   * Deleta um concorrente pelo seu ID.
   * 
   * @param id o ID do concorrente a ser deletado.
   * @return a resposta HTTP com o status 200 (OK) indicando que a operação foi concluída com sucesso.
   */
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    concorrenteService.deleteOrThrow(id);
    return ResponseEntity.ok().build();
  }

  /**
   * Método de fallback para quando a ID não é fornecida em uma requisição PUT.
   * 
   * @return a resposta HTTP com o status 404 (Not Found) indicando que o recurso não foi encontrado.
   */
  @PutMapping
  public ResponseEntity<Void> handleMissingId() {
    return ResponseEntity.notFound().build();
  }

  /**
   * Método de fallback para quando a ID não é fornecida em uma requisição DELETE.
   * 
   * @return a resposta HTTP com o status 404 (Not Found) indicando que o recurso não foi encontrado.
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteNoId() {
    return ResponseEntity.notFound().build();
  }
}
