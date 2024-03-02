package com.residenciatic18.apileilao.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Lance implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "leilao_id")
  private Leilao leilao;

  @ManyToOne
  @JoinColumn(name = "concorrente_id")
  private Concorrente concorrente;

  private Double valor;

  public Lance(Leilao leilao, Concorrente concorrente, Double valor) {
    this.leilao = leilao;
    this.concorrente = concorrente;
    this.valor = valor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}