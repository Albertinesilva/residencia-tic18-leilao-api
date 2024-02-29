package com.residenciatic18.apileilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Leilao;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

}