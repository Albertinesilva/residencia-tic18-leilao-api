package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;

public interface LanceService {

  List<Lance> buscarTodos(Long id);

  Lance salvar(Leilao leilao);

  Lance buscarPorId(Long id);

  Lance update(Long id, LanceForm leilaoForm);

  void delete(Long id);

  Boolean isExisteId(Long id);
}
