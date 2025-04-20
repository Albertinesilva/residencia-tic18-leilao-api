package com.residenciatic18.apileilao.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.residenciatic18.apileilao.entities.enums.deserializer.LeilaoStatusDeserializer;

@JsonDeserialize(using = LeilaoStatusDeserializer.class)
public enum LeilaoStatus {

  ABERTO("1"), FECHADO("2");

  private String code;

  private LeilaoStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @JsonCreator
  public static LeilaoStatus fromString(String code) {
    for (LeilaoStatus leilaoStatus : LeilaoStatus.values()) {
      if (leilaoStatus.code.equals(code)) {
        return leilaoStatus;
      }
    }
    throw new IllegalArgumentException("Invalid LeilaoStatus code");
  }

}