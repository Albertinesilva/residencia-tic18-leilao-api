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
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;
import com.residenciatic18.apileilao.web.dto.response.LeilaoResponseDto;

import jakarta.persistence.EntityNotFoundException;

/**
 * Controlador de operações relacionadas aos leilões.
 * 
 * A classe {@link LeilaoController} gerencia as requisições HTTP para a criação, leitura, atualização, 
 * e exclusão de leilões. Ela utiliza o serviço {@link LeilaoService} para a lógica de negócios e 
 * os DTOs {@link LeilaoForm} e {@link LeilaoResponseDto} para transferir os dados entre a API e o cliente.
 */
@RestController
@RequestMapping("/leilao/")
public class LeilaoController {

  @Autowired
  private LeilaoService leilaoService;

  /**
   * Cria um novo leilão.
   * 
   * @param createDto os dados do novo leilão no formato de DTO {@link LeilaoForm}.
   * @return a resposta HTTP com o status 201 (Created) e o DTO {@link LeilaoResponseDto} do leilão criado.
   *         Em caso de erro, retorna o status 400 (Bad Request).
   */
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

  /**
   * Busca um leilão pelo seu ID.
   * 
   * @param id o ID do leilão a ser buscado.
   * @return a resposta HTTP com o status 200 (OK) e os detalhes do leilão como {@link LeilaoResponseDto}.
   */
  @GetMapping("{id}")
  public ResponseEntity<List<LeilaoResponseDto>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(leilaoService.findByIdOrThrow(id));
  }

  /**
   * Retorna todos os leilões cadastrados.
   * 
   * @return a resposta HTTP com o status 200 (OK) e a lista de {@link LeilaoResponseDto}.
   */
  @GetMapping
  public ResponseEntity<List<LeilaoResponseDto>> searchAll() {
    return ResponseEntity.ok(LeilaoMapper.toListDto(leilaoService.findAll()));
  }

  /**
   * Atualiza um leilão existente.
   * 
   * @param id o ID do leilão a ser atualizado.
   * @param leilaoForm os dados de atualização no formato de DTO {@link LeilaoForm}.
   * @return a resposta HTTP com o status 200 (OK) e o DTO {@link LeilaoResponseDto} do leilão atualizado.
   */
  @PutMapping("{id}")
  public ResponseEntity<LeilaoResponseDto> update(@PathVariable Long id, @RequestBody LeilaoForm leilaoForm) {
    Leilao leilaoAtualizado = leilaoService.updateOrThrow(id, leilaoForm);
    LeilaoResponseDto dto = LeilaoMapper.toDto(leilaoAtualizado);
    return ResponseEntity.ok(dto);
  }

  /**
   * Deleta um leilão pelo seu ID.
   * 
   * @param id o ID do leilão a ser deletado.
   * @return a resposta HTTP com o status 204 (No Content) se a exclusão for bem-sucedida.
   *         Em caso de erro ou não encontrado, retorna o status 404 (Not Found).
   */
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    try {
      leilaoService.deleteOrThrow(id);
      return ResponseEntity.noContent().build(); // 204
    } catch (ResponseStatusException e) {
      return ResponseEntity.notFound().build(); // 404
    }
  }

  /**
   * Retorna os dados do vencedor de um leilão específico.
   * 
   * @param id o ID do leilão para o qual o vencedor será buscado.
   * @return a resposta HTTP com o status 200 (OK) e os dados do vencedor do leilão como um {@link Map}.
   *         Em caso de leilão não encontrado, retorna o status 404 (Not Found).
   *         Em caso de erro de estado, retorna o status 403 (Forbidden).
   */
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

  /**
   * Método de fallback para quando a rota "vencedor_leilao/" é chamada sem um ID.
   * 
   * @return a resposta HTTP com o status 400 (Bad Request) indicando que a requisição foi mal formada.
   */
  @GetMapping("vencedor_leilao/")
  public ResponseEntity<Void> AuctionWinne() {
    return ResponseEntity.badRequest().build();
  }
}
