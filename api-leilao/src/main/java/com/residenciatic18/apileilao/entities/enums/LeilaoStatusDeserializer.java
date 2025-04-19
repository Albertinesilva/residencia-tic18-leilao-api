package com.residenciatic18.apileilao.entities.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LeilaoStatusDeserializer extends JsonDeserializer<LeilaoStatus> {

  @Override
  public LeilaoStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String value = jsonParser.getValueAsString();
    for (LeilaoStatus leilaoStatus : LeilaoStatus.values()) {
      if (leilaoStatus.getCode().equals(value)) {
        return leilaoStatus;
      }
    }
    throw new IllegalArgumentException("Leilão status inválido: " + value);
  }
}
