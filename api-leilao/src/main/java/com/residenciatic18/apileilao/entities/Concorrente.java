package com.residenciatic18.apileilao.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_concorrente")
public class Concorrente extends AbstractEntity{

  private Long id;
  private String nome;
  private String cpf;

  @JsonIgnore
  @OneToMany(mappedBy = "concorrente")
  private List<Lance> lances = new ArrayList<>();

  public Concorrente(String nome, String cpf) {
    this.nome = nome;
    this.cpf = cpf;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}