package com.residenciatic18.apileilao.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.services.ConcorrenteService;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;

/**
 * Implementação do serviço de concorrentes.
 * 
 * A classe {@link ConcorrenteServiceImpl} implementa a interface {@link ConcorrenteService} e
 * fornece métodos para gerenciar os concorrentes, como salvar, atualizar, deletar, e buscar
 * concorrentes, além de realizar validações. As operações de leitura e escrita são gerenciadas
 * com transações e controle de exceções.
 */
@Service
public class ConcorrenteServiceImpl implements ConcorrenteService {

  @Autowired
  private ConcorrenteRepository concorrenteRepository;

  @Autowired
  private LanceRepository lanceRepository;

  /**
   * Salva um novo concorrente no repositório.
   * 
   * @param concorrente o concorrente a ser salvo.
   * @return o concorrente salvo.
   */
  @Override
  public Concorrente save(Concorrente concorrente) {
    return concorrenteRepository.save(concorrente);
  }

  /**
   * Busca um concorrente pelo ID. Lança uma exceção se o ID for inválido.
   * 
   * @param id o ID do concorrente a ser buscado.
   * @return o concorrente encontrado.
   * @throws IllegalArgumentException se o concorrente não for encontrado.
   */
  @Override
  @Transactional(readOnly = true)
  public Concorrente searchById(Long id) {
    return concorrenteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o concorrente:" + id));
  }

  /**
   * Busca concorrentes por ID ou retorna todos se o ID for nulo.
   * 
   * @param id o ID do concorrente a ser buscado (pode ser nulo).
   * @return a lista de {@link ConcorrenteResponseDto}.
   */
  @Override
  @Transactional(readOnly = true)
  public List<ConcorrenteResponseDto> searchDataByIDorAll(Long id) {
    if (id == null) {
      return ConcorrenteMapper.toListDto(findAll());
    } else {
      Concorrente concorrente = searchById(id);
      if (concorrente != null) {
        return ConcorrenteMapper.toListDto(Collections.singletonList(concorrente));
      } else {
        return new ArrayList<ConcorrenteResponseDto>();
      }
    }
  }

  /**
   * Retorna todos os concorrentes cadastrados.
   * 
   * @return lista de todos os concorrentes.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Concorrente> findAll() {
    return concorrenteRepository.findAll();
  }

  /**
   * Atualiza um concorrente existente com os dados fornecidos.
   * 
   * @param id o ID do concorrente a ser atualizado.
   * @param concorrenteForm os dados para atualizar o concorrente.
   * @return o concorrente atualizado.
   */
  @Override
  @Transactional(readOnly = false)
  public Concorrente update(Long id, ConcorrenteForm concorrenteForm) {
    Concorrente concorrente = searchById(id);
    concorrente.setNome(concorrenteForm.getNome());
    concorrente.setCpf(concorrenteForm.getCpf());
    return save(concorrente);
  }

  /**
   * Deleta um concorrente e todos os seus lances.
   * 
   * @param id o ID do concorrente a ser deletado.
   * @throws RuntimeException se o concorrente não for encontrado.
   */
  @Override
  @Transactional(readOnly = false)
  public void delete(Long id) {
    Concorrente concorrente = concorrenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Concorrente não encontrado"));

    // Deleta todos os lances relacionados a este concorrente
    lanceRepository.deleteByConcorrente(concorrente);

    // Agora pode excluir o concorrente
    concorrenteRepository.delete(concorrente);
  }

  /**
   * Busca um concorrente por ID e lança uma exceção se não encontrado.
   * 
   * @param id o ID do concorrente a ser buscado.
   * @return a lista de {@link ConcorrenteResponseDto}.
   * @throws ResponseStatusException se o concorrente não for encontrado.
   */
  @Override
  @Transactional(readOnly = true)
  public List<ConcorrenteResponseDto> findByIdOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Concorrente não encontrado");
    }
    return searchDataByIDorAll(id);
  }

  /**
   * Atualiza um concorrente e retorna a resposta com o {@link ConcorrenteResponseDto}.
   * Lança uma exceção se o concorrente não for encontrado.
   * 
   * @param id o ID do concorrente a ser atualizado.
   * @param form os dados para atualização.
   * @return o {@link ConcorrenteResponseDto} do concorrente atualizado.
   * @throws ResponseStatusException se o concorrente não for encontrado.
   */
  @Override
  @Transactional(readOnly = false)
  public ConcorrenteResponseDto updateOrThrow(Long id, ConcorrenteForm form) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Concorrente não encontrado");
    }

    Concorrente updated = update(id, form);
    return ConcorrenteMapper.toDto(updated);
  }

  /**
   * Deleta um concorrente e lança uma exceção caso não seja encontrado.
   * 
   * @param id o ID do concorrente a ser deletado.
   * @throws ResponseStatusException se o concorrente não for encontrado.
   */
  @Override
  @Transactional(readOnly = false)
  public void deleteOrThrow(Long id) {
    if (!isExisteId(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Concorrente não encontrado");
    }
    delete(id);
  }

  /**
   * Verifica se um concorrente com o ID fornecido existe.
   * 
   * @param id o ID do concorrente a ser verificado.
   * @return true se o concorrente existir, falso caso contrário.
   */
  @Override
  public boolean isExisteId(Long id) {
    return concorrenteRepository.existsById(id);
  }

  /**
   * Valida se um concorrente (identificado pelo seu ID) é válido.
   * 
   * @param bidderId o ID do concorrente a ser validado.
   * @return ResponseEntity com status adequado.
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Void> validateBidder(Long bidderId) {
    // Verifica se o concorrente existe
    if (!isExisteId(bidderId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Concorrente não encontrado
    }

    return ResponseEntity.ok().build(); // Concorrente válido
  }
}
