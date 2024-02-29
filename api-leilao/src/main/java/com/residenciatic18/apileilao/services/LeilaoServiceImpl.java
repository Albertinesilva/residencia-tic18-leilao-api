package com.residenciatic18.apileilao.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Aeroporto;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;
import com.residenciatic18.apileilao.web.dto.form.AeroportoForm;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;

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

  @Override
  public List<Leilao> buscarPorDescricao(String nome) {

    throw new UnsupportedOperationException("Unimplemented method 'buscarPorDescricao'");
  }

  @Override
  public Leilao buscarPorId(Long id) {

    return leilaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  public void delete(Long id) {
    leilaoRepository.deleteById(id);
  }

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
  public List<Leilao> buscarTodos(Long id) {

    List<Leilao> todosOsLeiloes = leilaoRepository.findAll();
    List<Leilao> leiloesEncontrados = new ArrayList<>();

    if (id != null) {

      for (Leilao leilao : todosOsLeiloes) {
        if (leilao.getId().equals(id)) {
          leiloesEncontrados.add(leilao);
          break; // Encontrou o leilão, então sai do loop
        }
      }

    } else {
      leiloesEncontrados.addAll(todosOsLeiloes); // Adiciona todos os leilões se o ID for nulo
    }
    return leiloesEncontrados;
  }

  @Override
  public Leilao update(Long id, LeilaoForm leilaoForm) {
    Leilao leilao = buscarPorId(id);
    leilao.setDescricrao(leilaoForm.getDescricrao());
    leilao.setValorMinimo(leilaoForm.getValorMinimo());
    leilao.setOrderStatus(leilaoForm.getOrderStatus());
    return salvar(leilao);
  }

}