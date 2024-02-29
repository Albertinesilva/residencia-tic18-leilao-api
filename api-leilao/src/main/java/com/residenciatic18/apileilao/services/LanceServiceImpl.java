package com.residenciatic18.apileilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;

public class LanceServiceImpl implements LanceService{

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public List<Lance> buscarTodos(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscarTodos'");
  }

  @Override
  public Lance salvar(Leilao leilao) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'salvar'");
  }

  @Override
  public Lance buscarPorId(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
  }

  @Override
  public Lance update(Long id, LanceForm leilaoForm) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public Boolean isExisteId(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isExisteId'");
  }
  
}
