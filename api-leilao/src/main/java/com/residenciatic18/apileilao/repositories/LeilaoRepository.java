package com.residenciatic18.apileilao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Leilao;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

  @Query("SELECT l FROM tb_leilao l JOIN FETCH l.lances tb_lance "
      + "WHERE l.id = :leilaoId "
      + "AND tb_lance.valor = (SELECT MAX(l.valor) FROM tb_lance l WHERE l.leilao.id = :leilaoId)")
  Optional<Leilao> findLeilaoWithMaiorLanceAndConcorrenteById(@Param("leilaoId") Long leilaoId);

  // @Query("SELECT l FROM tb_leilao l WHERE l.id = :id")
  // Optional<Leilao> findLeilaoWithMaiorLanceAndConcorrenteById(@Param("id") Long
  // id);
}