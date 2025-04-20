package com.residenciatic18.apileilao.entities;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Classe abstrata base para todas as entidades do sistema.
 * 
 * Esta classe é a superclasse comum para todas as entidades do modelo, fornecendo um identificador
 * único (ID) para cada entidade que herda dela. A utilização dessa classe abstrata permite evitar
 * duplicação de código nas entidades, promovendo reutilização e consistência.
 * 
 * A classe implementa a interface {@link Serializable} para garantir que os objetos dessa classe
 * possam ser serializados e desserializados de maneira eficiente.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

  /**
   * Identificador único da entidade.
   * Este campo é gerado automaticamente pela estratégia {@link GenerationType.IDENTITY}, ou seja,
   * o banco de dados é responsável por gerar o valor para esse campo.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Construtor padrão.
   * Utilizado para criar uma instância da classe {@link AbstractEntity}.
   */
  public AbstractEntity() {
    super();
  }

  /**
   * Construtor com ID.
   * Utilizado para criar uma instância da classe {@link AbstractEntity} com um ID específico.
   * 
   * @param id O identificador único a ser atribuído à entidade.
   */
  public AbstractEntity(Long id) {
    this.id = id;
  }

  /**
   * Obtém o identificador único da entidade.
   * 
   * @return O identificador da entidade.
   */
  public Long getId() {
    return id;
  }

  /**
   * Define o identificador único da entidade.
   * 
   * @param id O identificador a ser atribuído à entidade.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Verifica se a entidade não possui ID.
   * 
   * @return {@code true} se o ID for {@code null}, caso contrário {@code false}.
   */
  public boolean hasNotId() {
    return id == null;
  }

  /**
   * Verifica se a entidade possui ID.
   * 
   * @return {@code true} se o ID não for {@code null}, caso contrário {@code false}.
   */
  public boolean hasId() {
    return id != null;
  }

  /**
   * Calcula o código hash da entidade, levando em consideração apenas o ID.
   * O código hash é utilizado para comparar entidades em coleções baseadas em hash, como {@link java.util.HashSet}.
   * 
   * @return O código hash da entidade.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /**
   * Compara a entidade com outra entidade para verificar se são iguais.
   * A comparação é feita com base no ID da entidade. 
   * 
   * @param obj O objeto a ser comparado.
   * @return {@code true} se as entidades possuem o mesmo ID, caso contrário {@code false}.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractEntity other = (AbstractEntity) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /**
   * Retorna uma representação em string da entidade, incluindo o nome da classe e o ID.
   * Este método é útil para depuração e logging.
   * 
   * @return A representação em string da entidade, no formato "Entidade <nome da classe> id: <id>".
   */
  @Override
  public String toString() {
    return String.format("Entidade %s id: %s", this.getClass().getName(), getId());
  }

}
