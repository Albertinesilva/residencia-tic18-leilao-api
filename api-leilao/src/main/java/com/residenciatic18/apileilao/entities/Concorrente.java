package com.residenciatic18.apileilao.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_concorrente")
public class Concorrente extends AbstractEntity {
  
  private String nome;
  private String cpf;
}