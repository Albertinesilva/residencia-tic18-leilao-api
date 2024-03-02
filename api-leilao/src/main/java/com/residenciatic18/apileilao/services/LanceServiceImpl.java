package com.residenciatic18.apileilao.services;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.web.dto.LanceResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;

@Service
@Transactional(readOnly = false)
public class LanceServiceImpl implements LanceService {

  @Autowired
  private LanceRepository lanceRepository;

  @Autowired
  private LeilaoService leilaoService;

  @Autowired
  private ConcorrenteService concorrenteService;

  @SuppressWarnings("null")
  @Override
  @Transactional
  public Lance salvar(Lance lance) {
    return lanceRepository.save(lance);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> findById(Long id) {

    if (id == null) {
      return LanceMapper.toListDto(findAll());

    } else {

      Lance lance = buscarPorId(id);
      if (lance != null) {
        return LanceMapper.toListDto(Collections.singletonList(lance));

      } else {
        return Collections.emptyList();
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByLeilaoId(Long id) {
    Leilao leilao = new Leilao();
    leilao.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByLeilaoId(leilao.getId()));
  }

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> getByConcorrenteId(Long id) {
    Concorrente concorrente = new Concorrente();
    concorrente.setId(id);
    return LanceMapper.toListDto(lanceRepository.findByConcorrenteId(concorrente.getId()));
  }

  @Override
  public List<Lance> findAll() {
    return lanceRepository.findAll();
  }

  @SuppressWarnings("null")
  @Override
  public Lance buscarPorId(Long id) {
    return lanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o lance:" + id));
  }

  @Override
  public Lance update(Long id, LanceForm lanceForm) {

    Lance lance = buscarPorId(id);

    // Atualizar o leilão e o concorrente do lance
    Leilao leilao = leilaoService.buscarPorId(lanceForm.getLeilaoId());
    if (leilao == null) {
      throw new NoSuchElementException("Leilão com ID " + lanceForm.getLeilaoId() + " não encontrado");
    }
    lance.setLeilao(leilao);

    Concorrente concorrente = concorrenteService.buscarPorId(lanceForm.getConcorrenteId());
    if (concorrente == null) {
      throw new NoSuchElementException("Concorrente com ID " + lanceForm.getConcorrenteId() + " não encontrado");
    }
    lance.setConcorrente(concorrente);

    Double valor = lanceForm.getValor();
    if (valor == null || valor <= 0) {
      throw new IllegalArgumentException("O valor do lance deve ser maior que zero");
    }
    lance.setValor(valor);

    return salvar(lance);
  }

  @SuppressWarnings("null")
  @Override
  public void delete(Long id) {
    lanceRepository.deleteById(id);
  }

  @SuppressWarnings("null")
  @Override
  public Boolean isExisteId(Long id) {
    if (lanceRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

}