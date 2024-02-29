package com.residenciatic18.apileilao.entities;

import com.residenciatic18.apileilao.enums.LeilaoStatus;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_leilao")
public class Leilao extends AbstractEntity {

  private String descricrao;
  private Double valorMinimo;
  private Integer leilaoStatus;

  public Leilao(String descricrao, Double valorMinimo, LeilaoStatus leilaoStatus) {
    this.descricrao = descricrao;
    this.valorMinimo = valorMinimo;
    setOrderStatus(leilaoStatus);
  }

  public LeilaoStatus getOrderStatus() {
    return LeilaoStatus.valueOf(leilaoStatus);
  }

  public void setOrderStatus(LeilaoStatus orderStatus) {
    if (orderStatus != null) {
      this.leilaoStatus = orderStatus.getCode();
    }
  }
}