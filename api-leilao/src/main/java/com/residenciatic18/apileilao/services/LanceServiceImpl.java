package com.residenciatic18.apileilao.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.web.dto.LanceResponseDto;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.mapper.LanceMapper;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;

@Service
@Transactional(readOnly = false)
public class LanceServiceImpl implements LanceService {

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  @Transactional(readOnly = true)
  public List<LanceResponseDto> buscarTodos(Long id) {

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
  public List<Lance> findAll() {
    return lanceRepository.findAll();
  }

  @SuppressWarnings("null")
  @Override
  @Transactional
  public Lance salvar(Lance lance) {
    return lanceRepository.save(lance);
  }

  @Override
  public Lance buscarPorId(Long id) {
    return lanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  public Lance update(Long id, LanceForm lanceForm) {
    Lance lance = buscarPorId(id);
    lance.setConcorrente(lanceForm.getConcorrenteId().getId());
    lance.setLeilao(lanceForm.getLeilaoId().getId());
    lance.setValor(lanceForm.getValor());
    return salvar(lance);
  }

  @Override
  public void delete(Long id) {
    lanceRepository.deleteById(id);
  }

  @Override
  public Boolean isExisteId(Long id) {
    if (lanceRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

}