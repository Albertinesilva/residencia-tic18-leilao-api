package com.residenciatic18.apileilao.web.dto.form;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Leilao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LanceForm {

  // private Long leilaoId;
  // private Long concorrenteId;
  // private Double valor;

  private Leilao leilaoId;
  private Concorrente concorrenteId;
  private Double valor;

}