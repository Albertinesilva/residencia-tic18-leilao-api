package com.residenciatic18.apileilao.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;

@Service
@Transactional(readOnly = false)
public class ConcorrenteServiceImpl implements ConcorrenteService {

  @Autowired
  private ConcorrenteRepository concorrenteRepository;

  @SuppressWarnings("null")
  @Override
  @Transactional
  public Concorrente salvar(Concorrente concorrente) {
    return concorrenteRepository.save(concorrente);
  }

  @Override
  public Concorrente buscarPorId(Long id) {
    return concorrenteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  public void delete(Long id) {
    concorrenteRepository.deleteById(id);
  }

  @Override
  public Boolean isExisteId(Long id) {
    if (concorrenteRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Concorrente> buscarTodos(Long id) {

    List<Concorrente> todosOsLeiloes = concorrenteRepository.findAll();
    List<Concorrente> concorrentesEncontrados = new ArrayList<>();

    if (id != null) {

      for (Concorrente concorrentes : todosOsLeiloes) {
        if (concorrentes.getId().equals(id)) {
          concorrentesEncontrados.add(concorrentes);
          break;
        }
      }

    } else {
      concorrentesEncontrados.addAll(todosOsLeiloes); // Adiciona todos os leilões se o ID for nulo
    }
    return concorrentesEncontrados;
  }

  @Override
  public Concorrente update(Long id, ConcorrenteForm concorrenteForm) {
    Concorrente concorrente = buscarPorId(id);
    concorrente.setNome(concorrenteForm.getNome());
    concorrente.setCpf(concorrenteForm.getCpf());
    return salvar(concorrente);
  }

}
