package com.residenciatic18.apileilao.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.mapper.LeilaoMapper;

@Service
@Transactional(readOnly = false)
public class LeilaoServiceImpl implements LeilaoService {

  @Autowired
  private LeilaoRepository leilaoRepository;

  @Autowired
  private LanceRepository lanceRepository;

  @Override
  public Leilao salvar(Leilao leilao) {
    return leilaoRepository.save(leilao);
  }

  @Override
  @Transactional(readOnly = true)
  public Leilao buscarPorId(Long id) {
    return leilaoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id Inválido para o leilao:" + id));
  }

  @Override
  public void delete(Long id) {
    // Verificar se o leilão existe
    Leilao leilao = leilaoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Leilão não encontrado"));

    // Se não estiver usando cascata, você pode excluir os lances manualmente
    lanceRepository.deleteByLeilao(leilao);

    // Agora excluir o leilão
    leilaoRepository.delete(leilao);
  }

  @Override
  public boolean isExisteId(Long id) {
    return leilaoRepository.existsById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LeilaoResponseDto> buscarDtosPorIdOuTodos(Long id) {

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

  @Override
  @Transactional(readOnly = true)
  public List<Leilao> findAll() {
    return leilaoRepository.findAll();
  }

  @Override
  public Leilao update(Long id, LeilaoForm leilaoForm) {
    Leilao leilao = buscarPorId(id);
    leilao.setDescricao(leilaoForm.getDescricao());
    leilao.setValorMinimo(leilaoForm.getValorMinimo());
    leilao.setLeilaoStatus(leilaoForm.getLeilaoStatus());
    return salvar(leilao);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Leilao> vencedorDoLeilaoPorId(Long leilaoId) {
    return leilaoRepository.findLeilaoWithMaiorLanceAndConcorrenteById(leilaoId);
  }

}