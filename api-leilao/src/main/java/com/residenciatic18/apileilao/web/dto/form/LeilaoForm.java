package com.residenciatic18.apileilao.web.dto.form;

import com.residenciatic18.apileilao.enums.LeilaoStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LeilaoForm {

  private String descricrao;
  private Double valorMinimo;
  private Integer leilaoStatus;

  public LeilaoStatus getOrderStatus() {
    return LeilaoStatus.valueOf(leilaoStatus);
  }

  public void setOrderStatus(LeilaoStatus orderStatus) {
    if (orderStatus != null) {
      this.leilaoStatus = orderStatus.getCode();
    }
  }
}