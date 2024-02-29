package com.residenciatic18.apileilao.web.dto;

import com.residenciatic18.apileilao.entities.AbstractEntity;
import com.residenciatic18.apileilao.entities.Aeroporto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AeroportoResponseDto extends AbstractEntity {

  private String icao;

  public AeroportoResponseDto(Aeroporto aeroporto) {
    setId(aeroporto.getId());
    this.icao = aeroporto.getIcao();
  }
}