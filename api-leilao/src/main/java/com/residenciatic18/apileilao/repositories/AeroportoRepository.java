package com.residenciatic18.apileilao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Aeroporto;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, Long>{
    
  List<Aeroporto> findByIcao(String icao);

  List<Aeroporto> findByNome(String nome);

  List<Aeroporto> findByNomeAndIcao(String nome, String icao);
}
