package com.residenciatic18.apileilao.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.web.dto.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.mapper.ConcorrenteMapper;

@Service
public class ConcorrenteServiceImpl implements ConcorrenteService {

  @Autowired
  private ConcorrenteRepository concorrenteRepository;

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public Concorrente salvar(Concorrente concorrente) {
    return concorrenteRepository.save(concorrente);
  }

  @Override
  @Transactional(readOnly = true)
  public Concorrente buscarPorId(Long id) {
    return concorrenteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ConcorrenteResponseDto> buscarDtosPorIdOuTodos(Long id) {

    if (id == null) {
      return ConcorrenteMapper.toListDto(findAll());
    } else {

      Concorrente concorrente = buscarPorId(id);
      if (concorrente != null) {
        return ConcorrenteMapper.toListDto(Collections.singletonList(concorrente));

      } else {
        return new ArrayList<ConcorrenteResponseDto>();
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Concorrente> findAll() {
    return concorrenteRepository.findAll();
  }

  @Override
  @Transactional(readOnly = false)
  public Concorrente update(Long id, ConcorrenteForm concorrenteForm) {
    Concorrente concorrente = buscarPorId(id);
    concorrente.setNome(concorrenteForm.getNome());
    concorrente.setCpf(concorrenteForm.getCpf());
    return salvar(concorrente);
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(Long id) {
    Concorrente concorrente = concorrenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Concorrente não encontrado"));

    // Deleta todos os lances relacionados a este concorrente
    lanceRepository.deleteByConcorrente(concorrente);

    // Agora pode excluir o concorrente
    concorrenteRepository.delete(concorrente);
  }

  @Override
  public boolean isExisteId(Long id) {
    return concorrenteRepository.existsById(id);
  }

}