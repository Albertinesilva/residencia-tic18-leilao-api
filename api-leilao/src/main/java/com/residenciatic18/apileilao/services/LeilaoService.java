package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;

public interface LeilaoService {

  List<LeilaoResponseDto> findById(Long id);

  List<Leilao> findAll();

  Leilao salvar(Leilao leilao);

  Leilao buscarPorId(Long id);

  Leilao update(Long id, LeilaoForm leilaoForm);

  void delete(Long id);

  Boolean isExisteId(Long id);
}