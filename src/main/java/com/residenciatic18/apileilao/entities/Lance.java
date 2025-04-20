package com.residenciatic18.apileilao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um lance feito por um concorrente em um leilão.
 * 
 * A classe {@link Lance} herda de {@link AbstractEntity}, garantindo que cada lance tenha
 * um identificador único (ID). O lance é associado a um concorrente que o fez e a um leilão
 * específico no qual o lance foi registrado.
 * 
 * A classe é anotada com {@link Entity}, indicando que ela é uma entidade persistente
 * no banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_lance")
public class Lance extends AbstractEntity {

  /**
   * Leilão no qual o lance foi feito.
   * A anotação {@link ManyToOne} indica que um lance está associado a um único leilão.
   * A anotação {@link JoinColumn} define a coluna {@code leilao_id} na tabela de lances,
   * estabelecendo a chave estrangeira que liga o lance ao leilão.
   */
  @ManyToOne
  @JoinColumn(name = "leilao_id")
  private Leilao leilao;

  /**
   * Concorrente que fez o lance.
   * A anotação {@link ManyToOne} indica que um lance foi feito por um único concorrente.
   * A anotação {@link JoinColumn} define a coluna {@code concorrente_id} na tabela de lances,
   * estabelecendo a chave estrangeira que liga o lance ao concorrente.
   */
  @ManyToOne
  @JoinColumn(name = "concorrente_id")
  private Concorrente concorrente;

  /**
   * Valor do lance.
   * Este campo armazena o valor monetário oferecido pelo concorrente no leilão.
   */
  private Double valor;
}
