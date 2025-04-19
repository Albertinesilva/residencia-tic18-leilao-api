package com.residenciatic18.apileilao.web.dto;

import com.residenciatic18.apileilao.entities.AbstractEntity;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeilaoResponseDto extends AbstractEntity {

  private Long id;
  private String descricrao;
  private Double valorMinimo;
  private String leilaoStatus;

  public LeilaoResponseDto(Leilao leilao, LeilaoStatus leilaoStatus) {
    setId(leilao.getId());
    this.descricrao = leilao.getDescricao();
    this.valorMinimo = leilao.getValorMinimo();
    setLeilaoStatus(leilaoStatus);
  }

  public LeilaoStatus getLeilaoStatus() {
    return LeilaoStatus.fromString(leilaoStatus);
  }

  public void setLeilaoStatus(LeilaoStatus leilaoStatus) {
    if (leilaoStatus != null) {
      this.leilaoStatus = leilaoStatus.getCode();
    }
  }
}
