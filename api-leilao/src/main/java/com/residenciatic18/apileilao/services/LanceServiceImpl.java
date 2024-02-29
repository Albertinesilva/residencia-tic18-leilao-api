package com.residenciatic18.apileilao.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;

@Service
@Transactional(readOnly = false)
public class LanceServiceImpl implements LanceService {

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public List<Lance> buscarTodos(Long id) {
    List<Lance> todosOsLances = lanceRepository.findAll();
    List<Lance> lancesEncontrados = new ArrayList<>();

    if (id != null) {

      for (Lance lance : todosOsLances) {
        if (lance.getId().equals(id)) {
          lancesEncontrados.add(lance);
          break;
        }
      }

    } else {
      lancesEncontrados.addAll(todosOsLances);
    }
    return lancesEncontrados;
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