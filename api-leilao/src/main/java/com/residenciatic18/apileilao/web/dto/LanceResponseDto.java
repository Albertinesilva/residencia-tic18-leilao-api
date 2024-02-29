package com.residenciatic18.apileilao.web.dto;

import com.residenciatic18.apileilao.entities.AbstractEntity;
import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Leilao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LanceResponseDto extends AbstractEntity {

  private Long id;
  private Long leilaoId;
  private Long concorrenteId;
  private Double valor;

  public LanceResponseDto(Leilao leilao, Concorrente concorrente, Double valor) {
    setId(concorrente.getId());
    this.leilaoId = leilao.getId();
    this.concorrenteId = concorrente.getId();
    this.valor = valor;
  }

}