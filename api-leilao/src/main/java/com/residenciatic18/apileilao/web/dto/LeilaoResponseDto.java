package com.residenciatic18.apileilao.web.dto;

import com.residenciatic18.apileilao.entities.AbstractEntity;
import com.residenciatic18.apileilao.entities.Leilao;

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
  private Integer leilaoStatus;

  public LeilaoResponseDto(Leilao leilao) {
    setId(leilao.getId());
    this.descricrao = leilao.getDescricrao();
    this.valorMinimo = leilao.getValorMinimo();
    this.leilaoStatus = leilao.getLeilaoStatus();
  }
}
