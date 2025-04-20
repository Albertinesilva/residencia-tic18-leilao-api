package com.residenciatic18.apileilao.entities.enums.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import java.io.IOException;

/**
 * Deserializador customizado para a enumeração {@link LeilaoStatus}.
 * 
 * Essa classe é responsável por deserializar o código do status do leilão (em formato de string) e
 * converter para a enumeração correspondente {@link LeilaoStatus}.
 */
public class LeilaoStatusDeserializer extends JsonDeserializer<LeilaoStatus> {

  /**
   * Método responsável por desserializar a string de entrada e retornar o status correspondente.
   * 
   * A entrada é esperada ser um código de status (ex.: "1" ou "2") que será mapeado para a enumeração 
   * correspondente {@link LeilaoStatus}.
   *
   * @param p O parser de JSON que contém o texto a ser convertido.
   * @param ctxt O contexto de desserialização.
   * @return O status de leilão correspondente ao código passado.
   * @throws IOException Caso ocorra algum erro ao processar o texto de entrada.
   */
  @Override
  public LeilaoStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    // Obtém o texto do código de status a partir do parser
    String code = p.getText();
    
    // Converte o código para a enumeração LeilaoStatus correspondente
    return LeilaoStatus.fromString(code);
  }
}
