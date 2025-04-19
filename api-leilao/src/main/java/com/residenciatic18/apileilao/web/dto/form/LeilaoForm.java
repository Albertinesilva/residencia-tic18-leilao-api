package com.residenciatic18.apileilao.web.dto.form;

import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeilaoForm {

  private String descricao;
  private Double valorMinimo;
  private String leilaoStatus;

  public LeilaoStatus getLeilaoStatus() {
    return LeilaoStatus.fromString(leilaoStatus);
  }

  public void setLeilaoStatus(LeilaoStatus leilaoStatus) {
    if (leilaoStatus != null) {
      this.leilaoStatus = leilaoStatus.getCode();
    }
  }
}