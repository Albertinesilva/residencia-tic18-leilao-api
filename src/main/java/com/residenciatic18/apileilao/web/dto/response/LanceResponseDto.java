package com.residenciatic18.apileilao.web.dto.response;

import com.residenciatic18.apileilao.entities.AbstractEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LanceResponseDto extends AbstractEntity {

  private Long id;
  private Long leilaoId;
  private Long concorrenteId;
  private Double valor;

  public LanceResponseDto(Long id, Long leilaoId, Long concorrenteId, Double valor) {
    this.id = id;
    this.leilaoId = leilaoId;
    this.concorrenteId = concorrenteId;
    this.valor = valor;
  }

}