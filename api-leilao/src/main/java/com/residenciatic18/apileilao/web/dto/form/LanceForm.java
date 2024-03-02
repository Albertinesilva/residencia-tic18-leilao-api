package com.residenciatic18.apileilao.web.dto.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LanceForm {

  private Long leilaoId;
  private Long concorrenteId;
  private Double valor;
}