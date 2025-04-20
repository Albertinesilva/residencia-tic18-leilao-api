package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.form.LanceForm;
import com.residenciatic18.apileilao.web.dto.response.LanceResponseDto;

/**
 * Mapeador de entidades de lances.
 * 
 * A classe {@link LanceMapper} fornece métodos estáticos para realizar o mapeamento
 * entre entidades {@link Lance} e seus respectivos DTOs ({@link LanceForm} e 
 * {@link LanceResponseDto}). Além disso, ela oferece métodos para converter listas 
 * de entidades em listas de DTOs. O mapeamento é feito utilizando a biblioteca ModelMapper.
 */
public class LanceMapper {

  /**
   * Converte um objeto {@link LanceForm} para uma entidade {@link Lance}.
   * 
   * O mapeamento é feito manualmente, associando o leilão e o concorrente através dos seus respectivos IDs.
   *
   * @param createDto o objeto {@link LanceForm} a ser convertido.
   * @return a entidade {@link Lance} correspondente.
   */
  public static Lance toLance(LanceForm createDto) {
    return new Lance(new Leilao(createDto.getLeilaoId()), new Concorrente(createDto.getConcorrenteId()),
        createDto.getValor());
  }

  /**
   * Converte um objeto {@link Lance} para um DTO {@link LanceResponseDto}.
   *
   * @param lance a entidade {@link Lance} a ser convertida.
   * @return o DTO {@link LanceResponseDto} correspondente.
   */
  public static LanceResponseDto toDto(Lance lance) {
    PropertyMap<Lance, LanceResponseDto> props = new PropertyMap<Lance, LanceResponseDto>() {
      @Override
      protected void configure() {
        map().setId(source.getId());
        map().setLeilaoId(source.getLeilao().getId());
        map().setConcorrenteId(source.getConcorrente().getId());
        map().setValor(source.getValor());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(lance, LanceResponseDto.class);
  }

  /**
   * Converte uma lista de entidades {@link Lance} para uma lista de DTOs {@link LanceResponseDto}.
   *
   * @param lances a lista de entidades {@link Lance} a ser convertida.
   * @return a lista de DTOs {@link LanceResponseDto} correspondente.
   */
  public static List<LanceResponseDto> toListDto(List<Lance> lances) {
    return lances.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }
}

