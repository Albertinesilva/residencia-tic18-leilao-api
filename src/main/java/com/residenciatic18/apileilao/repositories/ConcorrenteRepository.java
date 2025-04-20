package com.residenciatic18.apileilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Concorrente;

@Repository
public interface ConcorrenteRepository extends JpaRepository<Concorrente, Long> {

}