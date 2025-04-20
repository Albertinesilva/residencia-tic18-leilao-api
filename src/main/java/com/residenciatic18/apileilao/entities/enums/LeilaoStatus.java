package com.residenciatic18.apileilao.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.residenciatic18.apileilao.entities.enums.deserializer.LeilaoStatusDeserializer;

/**
 * Enumeração que representa os possíveis status de um leilão.
 * 
 * Esta enumeração contém os status de um leilão, podendo ser:
 * - ABERTO: O leilão está aberto e aceitando lances.
 * - FECHADO: O leilão foi fechado e não aceita mais lances.
 * 
 * A enumeração é usada para indicar o status de cada leilão no sistema.
 */
@JsonDeserialize(using = LeilaoStatusDeserializer.class)
public enum LeilaoStatus {

  /**
   * Leilão aberto para lances.
   */
  ABERTO("1"),

  /**
   * Leilão fechado, sem mais possibilidade de lances.
   */
  FECHADO("2");

  /**
   * Código associado a cada status do leilão.
   */
  private String code;

  /**
   * Construtor que associa um código ao status do leilão.
   * 
   * @param code O código do status (1 para ABERTO, 2 para FECHADO).
   */
  private LeilaoStatus(String code) {
    this.code = code;
  }

  /**
   * Obtém o código associado ao status do leilão.
   * 
   * @return O código do status.
   */
  public String getCode() {
    return code;
  }

  /**
   * Método utilizado para desserializar o código do status a partir de uma string.
   * 
   * @param code O código do status a ser convertido.
   * @return O status correspondente ao código fornecido.
   * @throws IllegalArgumentException Se o código não corresponder a nenhum status válido.
   */
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
