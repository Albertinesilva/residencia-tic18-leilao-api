package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;

public interface ConcorrenteService {

  List<Concorrente> buscarTodos(Long id);

  Concorrente salvar(Concorrente aeroporto);

  Concorrente buscarPorId(Long id);

  Concorrente update(Long id, ConcorrenteForm aeroportoForm);

  void delete(Long id);

  Boolean isExisteId(Long id);
}