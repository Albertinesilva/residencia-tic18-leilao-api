package com.residenciatic18.apileilao.entities;

import java.util.ArrayList;
import java.util.List;

import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um leilão, que possui uma descrição, valor mínimo e status.
 * 
 * A classe {@link Leilao} herda de {@link AbstractEntity}, garantindo que cada leilão tenha
 * um identificador único (ID). O leilão pode ter vários lances associados a ele e seu status
 * pode ser manipulado através do tipo {@link LeilaoStatus}.
 * 
 * A classe é anotada com {@link Entity}, indicando que ela é uma entidade persistente
 * no banco de dados. O leilão tem uma relação com a entidade {@link Lance}, que representa
 * os lances feitos pelos concorrentes.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tb_leilao")
public class Leilao extends AbstractEntity {

  /**
   * Descrição do leilão.
   * Este campo contém uma descrição textual do leilão, como o item que está sendo leiloado.
   */
  private String descricao;

  /**
   * Valor mínimo do leilão.
   * Este campo armazena o valor mínimo exigido para o leilão.
   */
  private Double valorMinimo;

  /**
   * Status do leilão.
   * Este campo armazena o código do status do leilão, que é mapeado para um valor do enum {@link LeilaoStatus}.
   * O status pode ser "ABERTO" ou "FECHADO".
   */
  private String leilaoStatus;

  /**
   * Lista de lances associados ao leilão.
   * Um leilão pode ter múltiplos lances, representados pela relação {@link OneToMany} com a classe {@link Lance}.
   * A anotação {@link CascadeType.REMOVE} garante que, ao remover um leilão, todos os seus lances também serão removidos.
   */
  @OneToMany(mappedBy = "leilao", cascade = CascadeType.REMOVE)
  private List<Lance> lances = new ArrayList<>();

  /**
   * Construtor que recebe o ID do leilão.
   * @param id o ID do leilão.
   */
  public Leilao(Long id) {
    super(id);
  }

  /**
   * Construtor que recebe a descrição, valor mínimo e status do leilão.
   * @param descricao a descrição do leilão.
   * @param valorMinimo o valor mínimo do leilão.
   * @param leilaoStatus o status do leilão.
   */
  public Leilao(String descricao, Double valorMinimo, LeilaoStatus leilaoStatus) {
    this.descricao = descricao;
    this.valorMinimo = valorMinimo;
    setLeilaoStatus(leilaoStatus);
  }

  /**
   * Obtém o status do leilão como um valor do enum {@link LeilaoStatus}.
   * @return o status do leilão.
   */
  public LeilaoStatus getLeilaoStatus() {
    return LeilaoStatus.fromString(leilaoStatus);
  }

  /**
   * Define o status do leilão com base no enum {@link LeilaoStatus}.
   * @param leilaoStatus o status do leilão.
   */
  public void setLeilaoStatus(LeilaoStatus leilaoStatus) {
    if (leilaoStatus != null) {
      this.leilaoStatus = leilaoStatus.getCode();
    }
  }
}
