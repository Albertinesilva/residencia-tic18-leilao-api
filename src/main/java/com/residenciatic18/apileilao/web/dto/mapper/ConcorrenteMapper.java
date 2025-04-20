package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;
import com.residenciatic18.apileilao.web.dto.response.ConcorrenteResponseDto;

/**
 * Mapeador de entidades de concorrentes.
 * 
 * A classe {@link ConcorrenteMapper} fornece métodos estáticos para realizar o mapeamento
 * entre entidades {@link Concorrente} e seus respectivos DTOs ({@link ConcorrenteForm} e 
 * {@link ConcorrenteResponseDto}). Além disso, ela oferece métodos para converter listas 
 * de entidades em listas de DTOs. O mapeamento é feito utilizando a biblioteca ModelMapper.
 */
public class ConcorrenteMapper {

  /**
   * Converte um objeto {@link ConcorrenteForm} para uma entidade {@link Concorrente}.
   *
   * @param createDto o objeto {@link ConcorrenteForm} a ser convertido.
   * @return a entidade {@link Concorrente} correspondente.
   */
  public static Concorrente toConcorrente(ConcorrenteForm createDto) {
    return new ModelMapper().map(createDto, Concorrente.class);
  }

  /**
   * Converte um objeto {@link Concorrente} para um DTO {@link ConcorrenteResponseDto}.
   *
   * @param concorrente a entidade {@link Concorrente} a ser convertida.
   * @return o DTO {@link ConcorrenteResponseDto} correspondente.
   */
  public static ConcorrenteResponseDto toDto(Concorrente concorrente) {
    PropertyMap<Concorrente, ConcorrenteResponseDto> props = new PropertyMap<Concorrente, ConcorrenteResponseDto>() {
      @Override
      protected void configure() {
        map().setNome(source.getNome());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(concorrente, ConcorrenteResponseDto.class);
  }

  /**
   * Converte uma lista de entidades {@link Concorrente} para uma lista de DTOs {@link ConcorrenteResponseDto}.
   *
   * @param concorrentes a lista de entidades {@link Concorrente} a ser convertida.
   * @return a lista de DTOs {@link ConcorrenteResponseDto} correspondente.
   */
  public static List<ConcorrenteResponseDto> toListDto(List<Concorrente> concorrentes) {
    return concorrentes.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }
}
