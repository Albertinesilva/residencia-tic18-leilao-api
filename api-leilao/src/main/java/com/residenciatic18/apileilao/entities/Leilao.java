package com.residenciatic18.apileilao.entities;

import java.util.ArrayList;
import java.util.List;

import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_leilao")
public class Leilao extends AbstractEntity {

  private String descricao;
  private Double valorMinimo;
  private String leilaoStatus;

  @OneToMany(mappedBy = "leilao")
  private List<Lance> lances = new ArrayList<>();

  public Leilao(Long id) {
    super(id);
  }

  public Leilao(String descricrao, Double valorMinimo, LeilaoStatus leilaoStatus) {
    this.descricao = descricrao;
    this.valorMinimo = valorMinimo;
    setLeilaoStatus(leilaoStatus);
  }

  public LeilaoStatus getLeilaoStatus() {
    return LeilaoStatus.fromString(leilaoStatus);
  }

  public void setLeilaoStatus(LeilaoStatus leilaoStatus) {
    if (leilaoStatus != null) {
      this.leilaoStatus = leilaoStatus.getCode();
    }
  }
}