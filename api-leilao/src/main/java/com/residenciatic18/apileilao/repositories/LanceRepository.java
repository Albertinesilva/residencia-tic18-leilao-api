package com.residenciatic18.apileilao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {

  List<Lance> findByLeilaoId(Long leilaoId);

  List<Lance> findByConcorrenteId(Long concorrenteId);

  void deleteByLeilao(Leilao leilao);
}