package com.residenciatic18.apileilao.services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;
import com.residenciatic18.apileilao.services.LeilaoService;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;
import com.residenciatic18.apileilao.web.dto.response.LeilaoResponseDto;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementação do serviço de leilões.
 * 
 * A classe {@link LeilaoServiceImpl} implementa a interface {@link LeilaoService} e
 * fornece métodos para gerenciar os leilões, como salvar, atualizar, deletar, e buscar
 * leilões. Ela também inclui operações para verificar o vencedor do leilão, validar
 * o estado do leilão e realizar outras operações específicas, como o gerenciamento
 * de lances associados aos leilões. As operações de leitura e escrita são gerenciadas
 * com transações e controle de exceções.
 */
@Service
@Transactional(readOnly = false)
public class LeilaoServiceImpl implements LeilaoService {

  @Autowired
  private LeilaoRepository leilaoRepository;

  @Autowired
  private LanceRepository lanceRepository;

  /**
   * Salva um novo leilão ou atualiza um leilão existente no banco de dados.
   * 
   * @param leilao O leilão a ser salvo.
   * @return O leilão salvo.
   */
  @Override
  public Leilao save(Leilao leilao) {
    return leilaoRepository.save(leilao);
  }

  /**
   * Busca um leilão pelo seu ID. Caso não exista, lança uma exceção.
   * 
   * @param id O ID do leilão a ser buscado.
   * @return O leilão encontrado.
   * @throws IllegalArgumentException Caso o ID não seja válido.
   */
  @Override
  @Transactional(readOnly = true)
  public Leilao searchById(Long id) {
    return leilaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  /**
   * Busca leilões por ID, ou retorna todos os leilões caso o ID seja nulo.
   * 
   * @param id O ID do leilão a ser buscado. Caso seja nulo, retorna todos os leilões.
   * @return Uma lista de DTOs de leilões.
   */
  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> searchDataByIDorAll(Long id) {

    if (id == null) {
      return LeilaoMapper.toListDto(leilaoRepository.findAll());

    } else {

      Leilao leilao = searchById(id);
      if (leilao != null) {
        return LeilaoMapper.toListDto(Collections.singletonList(leilao));

      } else {
        return Collections.emptyList();
      }
    }
  }

  /**
   * Retorna todos os leilões cadastrados no banco de dados.
   * 
   * @return Uma lista de leilões.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Leilao> findAll() {
    return leilaoRepository.findAll();
  }

  /**
   * Atualiza um leilão com os dados fornecidos no formulário.
   * 
   * @param id O ID do leilão a ser atualizado.
   * @param leilaoForm Os dados do leilão a serem atualizados.
   * @return O leilão atualizado.
   */
  @Override
  public Leilao update(Long id, LeilaoForm leilaoForm) {
    Leilao leilao = searchById(id);
    leilao.setDescricao(leilaoForm.getDescricao());
    leilao.setValorMinimo(leilaoForm.getValorMinimo());
    leilao.setLeilaoStatus(leilaoForm.getLeilaoStatus());
    return save(leilao);
  }

  /**
   * Exclui um leilão pelo ID, juntamente com os lances relacionados a ele.
   * 
   * @param id O ID do leilão a ser excluído.
   * @throws RuntimeException Caso o leilão não seja encontrado.
   */
  @Override
  public void delete(Long id) {
    // Verificar se o leilão existe
    Leilao leilao = leilaoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

    // Se não estiver usando cascata, você pode excluir os lances manualmente
    lanceRepository.deleteByLeilao(leilao);

    // Agora excluir o leilão
    leilaoRepository.delete(leilao);
  }

  /**
   * Obtém os dados do vencedor de um leilão, incluindo o maior lance e o concorrente.
   * 
   * @param leilaoId O ID do leilão.
   * @return Um mapa contendo o leilão, o maior lance e o concorrente.
   * @throws EntityNotFoundException Caso o leilão não exista ou não tenha lances.
   * @throws IllegalStateException Caso o leilão esteja fechado.
   */
  @Override
  @Transactional(readOnly = true)
  public Map<String, Object> obterDadosDoVencedor(Long leilaoId) {
    if (!isExisteId(leilaoId)) {
      throw new EntityNotFoundException("Leilão não encontrado");
    }

    Leilao leilao = searchById(leilaoId);

    if (LeilaoStatus.FECHADO.equals(leilao.getLeilaoStatus())) {
      throw new IllegalStateException("Leilão está fechado");
    }

    Optional<Leilao> optionalLeilao = winnerOfAuctionById(leilaoId);
    if (optionalLeilao.isEmpty()) {
      throw new EntityNotFoundException("Leilão sem lances");
    }

    Leilao leilaoComMaiorLance = optionalLeilao.get();
    Double maiorLanceValor = leilaoComMaiorLance.getLances().stream()
        .mapToDouble(Lance::getValor)
        .max()
        .orElse(0.0);

    Concorrente concorrente = leilaoComMaiorLance.getLances().stream()
        .filter(lance -> lance.getValor().equals(maiorLanceValor))
        .findFirst()
        .map(Lance::getConcorrente)
        .orElse(null);

    Map<String, Object> response = new HashMap<>();
    response.put("leilao", LeilaoMapper.toDto(leilaoComMaiorLance));
    response.put("maiorLance", maiorLanceValor);
    response.put("concorrente", concorrente != null ? ConcorrenteMapper.toDto(concorrente) : null);

    return response;
  }

  /**
   * Retorna o leilão com o maior lance e o concorrente associado, caso existam.
   * 
   * @param leilaoId O ID do leilão.
   * @return Um {@link Optional} contendo o leilão com o maior lance.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<Leilao> winnerOfAuctionById(Long leilaoId) {
    return leilaoRepository.findLeilaoWithMaiorLanceAndConcorrenteById(leilaoId);
  }

  /**
   * Verifica se um leilão existe pelo ID. Caso não exista, lança uma exceção de status.
   * 
   * @param id O ID do leilão.
   * @return Uma lista de DTOs de leilão.
   * @throws ResponseStatusException Caso o leilão não seja encontrado.
   */
  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> findByIdOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    return searchDataByIDorAll(id);
  }

  /**
   * Atualiza um leilão ou lança uma exceção caso não seja encontrado.
   * 
   * @param id O ID do leilão a ser atualizado.
   * @param leilaoForm Os dados do leilão a serem atualizados.
   * @return O leilão atualizado.
   * @throws ResponseStatusException Caso o leilão não seja encontrado.
   */
  @Override
  @Transactional(readOnly = false)
  public Leilao updateOrThrow(Long id, LeilaoForm leilaoForm) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    return update(id, leilaoForm); // assuming update() performs the update
  }

  /**
   * Deleta um leilão ou lança uma exceção caso não seja encontrado.
   * 
   * @param id O ID do leilão a ser deletado.
   * @throws ResponseStatusException Caso o leilão não seja encontrado.
   */
  @Override
  @Transactional(readOnly = false)
  public void deleteOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found");
    }
    delete(id); // assuming delete() performs the actual deletion
  }

  /**
   * Valida se o leilão está aberto para ser manipulado. 
   * Caso o leilão não exista ou esteja fechado, retorna um erro.
   * 
   * @param auctionId O ID do leilão a ser validado.
   * @return Uma resposta HTTP com o status do leilão.
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Void> validateAuction(Long auctionId) {
    // Check if the auction exists
    if (!isExisteId(auctionId)) {
      return ResponseEntity.badRequest().build(); // Auction not found
    }

    // Check if the auction is closed
    Leilao leilao = searchById(auctionId);
    if (leilao.getLeilaoStatus() == LeilaoStatus.FECHADO) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Auction is closed
    }

    return ResponseEntity.ok().build(); // Auction is valid
  }

  /**
   * Verifica se um leilão existe no banco de dados.
   * 
   * @param id O ID do leilão a ser verificado.
   * @return {@code true} se o leilão existir, caso contrário {@code false}.
   */
  @Override
  public boolean isExisteId(Long id) {
    return leilaoRepository.existsById(id);
  }

}
