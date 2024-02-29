package com.residenciatic18.apileilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residenciatic18.apileilao.entities.Lance;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long>{
  
}