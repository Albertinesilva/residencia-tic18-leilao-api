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
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.response.LanceResponseDto;

/**
 * Controlador de operações relacionadas aos lances.
 * 
 * A classe {@link LanceController} gerencia as requisições HTTP relacionadas ao 
 * gerenciamento de lances em leilões. Ela oferece endpoints para criar, ler, atualizar 
 * e deletar informações de lances, utilizando o serviço {@link LanceService} para a lógica de negócios. 
 * Os dados são transferidos entre a API e o cliente usando DTOs ({@link LanceForm}, {@link LanceResponseDto}, 
 * {@link ConcorrenteResponseDto}).
 */
@RestController
@RequestMapping("/lance/")
public class LanceController {

  @Autowired
  private LanceService lanceService;

  /**
   * Cria um novo lance.
   * 
   * @param createDto os dados do novo lance no formato de DTO.
   * @return a resposta HTTP com o status do lance criado.
   */
  @PostMapping("create")
  public ResponseEntity<?> create(@RequestBody LanceForm createDto) {
    return lanceService.createBid(createDto);
  }

  /**
   * Busca um lance pelo seu ID.
   * 
   * @param id o ID do lance a ser buscado.
   * @return a resposta HTTP com o status 200 (OK) e a lista de {@link LanceResponseDto} correspondente.
   */
  @GetMapping("{id}")
  public ResponseEntity<List<LanceResponseDto>> getById(@PathVariable Long id) {
    return lanceService.getByIdOrNotFound(id);
  }

  /**
   * Retorna todos os lances cadastrados.
   * 
   * @return a resposta HTTP com o status 200 (OK) e a lista de {@link LanceResponseDto}.
   */
  @GetMapping()
  public ResponseEntity<List<LanceResponseDto>> searchAll() {
    return ResponseEntity.ok(LanceMapper.toListDto(lanceService.findAll()));
  }

  /**
   * Busca lances de um leilão específico pelo ID do leilão.
   * 
   * @param id o ID do leilão para o qual os lances serão buscados.
   * @return a resposta HTTP com o status 200 (OK) e os lances encontrados.
   */
  @GetMapping("/leilao={id}")
  public ResponseEntity<?> getByLeilaoId(@PathVariable Long id) {
    return lanceService.getByLeilaoIdOrNotFound(id);
  }

  /**
   * Busca lances de um concorrente específico pelo ID do concorrente.
   * 
   * @param id o ID do concorrente para o qual os lances serão buscados.
   * @return a resposta HTTP com o status 200 (OK) e os lances encontrados.
   */
  @GetMapping("/concorrente={id}")
  public ResponseEntity<?> getByConcorrenteId(@PathVariable Long id) {
    return lanceService.getByConcorrenteIdOrNotFound(id);
  }

  /**
   * Atualiza um lance existente.
   * 
   * @param id o ID do lance a ser atualizado.
   * @param createDto os dados de atualização no formato de DTO {@link LanceForm}.
   * @return a resposta HTTP com o status 200 (OK) e o objeto {@link LanceResponseDto} do lance atualizado.
   */
  @PutMapping("{id}")
  public ResponseEntity<LanceResponseDto> update(@PathVariable Long id, @RequestBody LanceForm createDto) {
    return lanceService.updateBid(id, createDto);
  }

  /**
   * Deleta um lance pelo seu ID.
   * 
   * @param id o ID do lance a ser deletado.
   * @return a resposta HTTP com o status 200 (OK) indicando que o lance foi excluído com sucesso.
   *         Retorna um {@link ConcorrenteResponseDto} atualizado após a exclusão.
   */
  @DeleteMapping("{id}")
  public ResponseEntity<ConcorrenteResponseDto> delete(@PathVariable("id") Long id) {

    // Delegando a verificação e exclusão para o serviço
    Optional<ConcorrenteResponseDto> concorrenteDtoOpt = lanceService.deleteBid(id);

    if (concorrenteDtoOpt.isEmpty()) {
      return ResponseEntity.notFound().build(); // Lance não encontrado ou erro ao excluir
    }

    return ResponseEntity.ok(concorrenteDtoOpt.get()); // Retorna o DTO atualizado
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
