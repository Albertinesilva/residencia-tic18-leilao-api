package com.residenciatic18.apileilao.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um concorrente que participa de leilões, oferecendo lances.
 * 
 * A classe {@link Concorrente} herda de {@link AbstractEntity}, garantindo que cada concorrente tenha
 * um identificador único (ID). Um concorrente é identificado por seu nome e CPF, e pode realizar
 * múltiplos lances em diversos leilões.
 * 
 * A classe é anotada com {@link Entity}, indicando que ela é uma entidade persistente no banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_concorrente")
public class Concorrente extends AbstractEntity {

  /**
   * Nome do concorrente.
   * Este campo representa o nome do concorrente que participa dos leilões.
   */
  private String nome;

  /**
   * CPF do concorrente.
   * Este campo armazena o CPF do concorrente, que deve ser único para cada concorrente.
   */
  private String cpf;

  /**
   * Lista de lances realizados pelo concorrente.
   * A anotação {@link JsonIgnore} evita que esta lista seja serializada na resposta JSON, pois
   * pode gerar recursão infinita com a relação bidirecional entre {@link Concorrente} e {@link Lance}.
   * 
   * A anotação {@link OneToMany} indica que um concorrente pode ter vários lances, e a
   * propriedade {@code mappedBy} faz referência à relação inversa na classe {@link Lance}.
   * A cascata {@link CascadeType.REMOVE} garante que os lances sejam removidos automaticamente
   * quando o concorrente for excluído.
   */
  @JsonIgnore
  @OneToMany(mappedBy = "concorrente", cascade = CascadeType.REMOVE)
  private List<Lance> lances = new ArrayList<>();

  /**
   * Construtor com ID.
   * Este construtor é utilizado para criar um concorrente com um ID já existente.
   * 
   * @param id O identificador único do concorrente.
   */
  public Concorrente(Long id) {
    super(id);
  }

  /**
   * Construtor principal.
   * Este construtor é utilizado para criar um novo concorrente com um nome e CPF específicos.
   * 
   * @param nome O nome do concorrente.
   * @param cpf O CPF do concorrente.
   */
  public Concorrente(String nome, String cpf) {
    this.nome = nome;
    this.cpf = cpf;
  }
}
