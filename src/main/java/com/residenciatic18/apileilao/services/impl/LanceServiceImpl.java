package com.residenciatic18.apileilao.services.impl;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.services.LanceService;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.response.LanceResponseDto;

/**
 * Implementação do serviço de lances.
 * 
 * A classe {@link LanceServiceImpl} implementa a interface {@link LanceService} e
 * fornece métodos para gerenciar os lances, como salvar, atualizar, deletar, e buscar
 * lances, além de realizar validações. As operações de leitura e escrita são gerenciadas
 * com transações e controle de exceções.
 */
@Service
@Transactional(readOnly = false)
public class LanceServiceImpl implements LanceService {

  @Autowired
  private LanceRepository lanceRepository;

  @Autowired
  private LeilaoService leilaoService;

  @Autowired
  private ConcorrenteService concorrenteService;

  /**
   * Salva um novo lance no banco de dados.
   *
   * @param lance O objeto Lance a ser salvo.
   * @return O lance salvo.
   */
  @Override
  @Transactional
  public Lance save(Lance lance) {
    return lanceRepository.save(lance);
  }

  /**
   * Busca lances por ID ou retorna todos os lances.
   *
   * @param id O ID do lance a ser buscado. Se nulo, retorna todos os lances.
   * @return Lista de lances em formato DTO.
   */
  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> searchDataByIDorAll(Long id) {

    if (id == null) {
      return LanceMapper.toListDto(findAll());

    } else {

      Lance lance = searchById(id);
      if (lance != null) {
        return LanceMapper.toListDto(Collections.singletonList(lance));

      } else {
        return Collections.emptyList();
      }
    }
  }

  /**
   * Busca lances associados a um leilão pelo ID do leilão.
   *
   * @param id O ID do leilão.
   * @return Lista de lances em formato DTO.
   */
  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByLeilaoId(Long id) {
    Leilao leilao = new Leilao();
    leilao.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByLeilaoId(leilao.getId()));
  }

  /**
   * Busca lances associados a um concorrente pelo ID do concorrente.
   *
   * @param id O ID do concorrente.
   * @return Lista de lances em formato DTO.
   */
  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByConcorrenteId(Long id) {
    Concorrente concorrente = new Concorrente();
    concorrente.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByConcorrenteId(concorrente.getId()));
  }

  /**
   * Retorna todos os lances armazenados no banco de dados.
   *
   * @return Lista de todos os lances.
   */
  @Override
  public List<Lance> findAll() {
    return lanceRepository.findAll();
  }

  /**
   * Busca um lance pelo ID. Lança uma exceção se não encontrar.
   *
   * @param id O ID do lance.
   * @return O lance correspondente ao ID fornecido.
   * @throws IllegalArgumentException Se o lance não for encontrado.
   */
  @Override
  public Lance searchById(Long id) {
    return lanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o lance:" + id));
  }

  /**
   * Atualiza um lance existente com os dados fornecidos.
   *
   * @param id        O ID do lance a ser atualizado.
   * @param lanceForm O formulário contendo os dados do lance a ser atualizado.
   * @return O lance atualizado.
   * @throws IllegalArgumentException Se o valor do lance for inválido (menor ou igual a zero).
   * @throws NoSuchElementException   Se o leilão ou concorrente não forem encontrados.
   */
  @Override
  public Lance update(Long id, LanceForm lanceForm) {

    Lance lance = searchById(id);

    // Atualizar o leilão e o concorrente do lance
    Leilao leilao = leilaoService.searchById(lanceForm.getLeilaoId());
    if (leilao == null) {
      throw new NoSuchElementException("Leilão com ID " + lanceForm.getLeilaoId() + " não encontrado");
    }
    lance.setLeilao(leilao);

    Concorrente concorrente = concorrenteService.searchById(lanceForm.getConcorrenteId());
    if (concorrente == null) {
      throw new NoSuchElementException("Concorrente com ID " + lanceForm.getConcorrenteId() + " não encontrado");
    }
    lance.setConcorrente(concorrente);

    Double valor = lanceForm.getValor();
    if (valor == null || valor <= 0) {
      throw new IllegalArgumentException("O valor do lance deve ser maior que zero");
    }
    lance.setValor(valor);

    return save(lance);
  }

  /**
   * Exclui um lance pelo ID.
   *
   * @param id O ID do lance a ser excluído.
   */
  @Override
  public void delete(Long id) {
    lanceRepository.deleteById(id);
  }

  /**
   * Verifica se um lance com o ID fornecido existe.
   *
   * @param id O ID do lance.
   * @return True se o lance existir, caso contrário, false.
   */
  @Override
  public Boolean isExisteId(Long id) {
    return lanceRepository.existsById(id);
  }

  /**
   * Retorna o lance com o maior valor de um leilão específico.
   *
   * @param leilaoId O ID do leilão.
   * @return O lance com o maior valor ou null se não houver um lance maior.
   */
  @Override
  public Lance findHighestBidByAuctionId(Long leilaoId) {
    Lance lance = searchById(leilaoId);

    if (lance.getValor() == lanceRepository.findAll().stream().mapToDouble(Lance::getValor).max().getAsDouble()) {
      return lance;
    } else {
      return null;
    }
  }

  /**
   * Cria um novo lance baseado nos dados fornecidos no formulário.
   *
   * @param createDto O DTO contendo os dados para criação do lance.
   * @return A resposta com o lance criado e o URI correspondente.
   */
  @Override
  @Transactional
  public ResponseEntity<?> createBid(LanceForm createDto) {
    // Validações para leilão e concorrente
    ResponseEntity<Void> auctionValidation = leilaoService.validateAuction(createDto.getLeilaoId());
    if (!auctionValidation.getStatusCode().is2xxSuccessful()) {
      return auctionValidation;
    }

    ResponseEntity<Void> bidderValidation = concorrenteService.validateBidder(createDto.getConcorrenteId());
    if (!bidderValidation.getStatusCode().is2xxSuccessful()) {
      return bidderValidation;
    }

    // Cria e salva o lance
    Lance lance = lanceRepository.save(LanceMapper.toLance(createDto));

    // Constrói o URI de resposta
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(lance.getId())
        .toUri();

    return ResponseEntity.created(uri).body(LanceMapper.toDto(lance));
  }

  /**
   * Busca lances pelo ID, retornando um erro 404 se não encontrado.
   *
   * @param id O ID do lance.
   * @return A resposta com os lances encontrados ou um erro 404.
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByIdOrNotFound(Long id) {
    if (!isExisteId(id)) {
      return ResponseEntity.notFound().build();
    }
    List<LanceResponseDto> response = searchDataByIDorAll(id);
    return ResponseEntity.ok(response);
  }

  /**
   * Busca lances pelo ID do leilão, retornando um erro 404 se não encontrado.
   *
   * @param id O ID do leilão.
   * @return A resposta com os lances encontrados ou um erro 404.
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByLeilaoIdOrNotFound(Long id) {
    List<LanceResponseDto> lances = getByLeilaoId(id);
    if (lances.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(lances);
  }

  /**
   * Busca lances pelo ID do concorrente, retornando um erro 404 se não encontrado.
   *
   * @param id O ID do concorrente.
   * @return A resposta com os lances encontrados ou um erro 404.
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<List<LanceResponseDto>> getByConcorrenteIdOrNotFound(Long id) {
    List<LanceResponseDto> lances = getByConcorrenteId(id);
    if (lances.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(lances);
  }

  /**
   * Atualiza um lance existente com os dados fornecidos, realizando validações.
   *
   * @param id        O ID do lance a ser atualizado.
   * @param createDto O DTO contendo os dados do lance.
   * @return A resposta com o lance atualizado.
   */
  @Override
  @Transactional(readOnly = false)
  public ResponseEntity<LanceResponseDto> updateBid(Long id, LanceForm createDto) {
    // Verifica as condições de validação do leilão e concorrente
    if (!leilaoService.isExisteId(createDto.getLeilaoId())) {
      return ResponseEntity.badRequest().build(); // Leilão não encontrado
    }

    Leilao leilao = leilaoService.searchById(createDto.getLeilaoId());
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Leilão fechado
    }

    if (!concorrenteService.isExisteId(createDto.getConcorrenteId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Concorrente não encontrado
    }

    // Verifica se o lance existe
    Optional<Lance> existingLanceOpt = lanceRepository.findById(id);
    if (!existingLanceOpt.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Lance não encontrado
    }

    // Atualiza o lance
    Lance updatedLance = update(id, createDto);

    // Retorna o lance atualizado
    return ResponseEntity.ok(LanceMapper.toDto(updatedLance));
  }

  /**
   * Exclui um lance existente, se possível, e retorna o DTO atualizado do concorrente.
   *
   * @param id O ID do lance a ser excluído.
   * @return O DTO do concorrente atualizado.
   */
  @Override
  @Transactional(readOnly = false)
  public Optional<ConcorrenteResponseDto> deleteBid(Long id) {
    // Verifica se o lance existe
    Optional<Lance> lanceOpt = lanceRepository.findById(id);
    if (lanceOpt.isEmpty()) {
      return Optional.empty(); // Lance não encontrado
    }

    Lance lance = lanceOpt.get();

    // Verifica se o leilão associado ao lance está fechado
    Leilao leilao = leilaoService.searchById(lance.getLeilao().getId());
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return Optional.empty(); // Leilão fechado, não pode excluir o lance
    }

    // Exclui o lance
    lanceRepository.delete(lance);

    // Retorna o DTO atualizado do concorrente
    ConcorrenteResponseDto concorrenteDto = ConcorrenteMapper.toDto(lance.getConcorrente());
    return Optional.of(concorrenteDto);
  }
}
