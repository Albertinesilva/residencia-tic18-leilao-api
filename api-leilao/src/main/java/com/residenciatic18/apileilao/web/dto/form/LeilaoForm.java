package com.residenciatic18.apileilao.web.dto.form;

import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LeilaoForm {

  private String descricrao;
  private Double valorMinimo;
  private Integer leilaoStatus;

  public LeilaoStatus getLeilaoStatus() {
    return LeilaoStatus.valueOf(leilaoStatus);
  }

  public void setLeilaoStatus(LeilaoStatus leilaoStatus) {
    if (leilaoStatus != null) {
      this.leilaoStatus = leilaoStatus.getCode();
    }
  }
}