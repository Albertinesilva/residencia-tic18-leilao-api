package com.residenciatic18.apileilao.enums;

public enum LeilaoStatus {

  ABERTO(1), FECHADO(2);

  private int code;

  private LeilaoStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static LeilaoStatus valueOf(int code) {
    for (LeilaoStatus value : LeilaoStatus.values()) {
      if (value.getCode() == code) {
        return value;
      }
    }
    throw new IllegalArgumentException("Invalid LeilaoStatus code");
  }
}