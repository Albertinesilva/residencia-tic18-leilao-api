package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.web.dto.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;

public interface ConcorrenteService {

  Concorrente save(Concorrente aeroporto);

  List<ConcorrenteResponseDto> searchDataByIDorAll(Long id);

  List<Concorrente> findAll();

  Concorrente searchById(Long id);

  Concorrente update(Long id, ConcorrenteForm aeroportoForm);

  void delete(Long id);

  boolean isExisteId(Long id);
}