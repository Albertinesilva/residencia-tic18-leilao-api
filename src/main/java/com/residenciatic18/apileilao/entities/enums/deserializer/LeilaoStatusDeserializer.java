package com.residenciatic18.apileilao.entities.enums.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import java.io.IOException;

public class LeilaoStatusDeserializer extends JsonDeserializer<LeilaoStatus> {

  @Override
  public LeilaoStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String code = p.getText();
    return LeilaoStatus.fromString(code);
  }
}
