package com.residenciatic18.apileilao.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;

@Service
@Transactional(readOnly = false)
public class LeilaoServiceImpl implements LeilaoService {

  @Autowired
  private LeilaoRepository leilaoRepository;

  @SuppressWarnings("null")
  @Override
  @Transactional
  public Leilao salvar(Leilao leilao) {
    return leilaoRepository.save(leilao);
  }

  @SuppressWarnings("null")
  @Override
  public Leilao buscarPorId(Long id) {
    return leilaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @SuppressWarnings("null")
  @Override
  public void delete(Long id) {
    leilaoRepository.deleteById(id);
  }

  @SuppressWarnings("null")
  @Override
  public Boolean isExisteId(Long id) {
    if (leilaoRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> findById(Long id) {

    if (id == null) {
      return LeilaoMapper.toListDto(leilaoRepository.findAll());

    } else {

      Leilao leilao = buscarPorId(id);
      if (leilao != null) {
        return LeilaoMapper.toListDto(Collections.singletonList(leilao));

      } else {
        return Collections.emptyList();
      }
    }
  }

  public List<Leilao> findAll() {
    return leilaoRepository.findAll();
  }

  @Override
  public Leilao update(Long id, LeilaoForm leilaoForm) {
    Leilao leilao = buscarPorId(id);
    leilao.setDescricrao(leilaoForm.getDescricrao());
    leilao.setValorMinimo(leilaoForm.getValorMinimo());
    leilao.setLeilaoStatus(leilaoForm.getLeilaoStatus());
    return salvar(leilao);
  }
  
}