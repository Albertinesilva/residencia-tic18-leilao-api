package com.residenciatic18.apileilao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_lance")
public class Lance extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "leilao_id")
  private Leilao leilao;

  @ManyToOne
  @JoinColumn(name = "concorrente_id")
  private Concorrente concorrente;

  private Double valor;

}