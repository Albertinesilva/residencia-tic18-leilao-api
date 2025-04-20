package com.residenciatic18.apileilao.entities.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LeilaoStatusDeserializer extends JsonDeserializer<LeilaoStatus> {

  @Override
  public LeilaoStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String code = p.getText();
    return LeilaoStatus.fromString(code);
  }
}
