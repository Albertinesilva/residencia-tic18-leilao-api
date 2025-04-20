package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;
import com.residenciatic18.apileilao.web.dto.response.LeilaoResponseDto;

/**
 * Mapeador de entidades de leilões.
 * 
 * A classe {@link LeilaoMapper} fornece métodos estáticos para realizar o mapeamento
 * entre entidades {@link Leilao} e seus respectivos DTOs ({@link LeilaoForm} e 
 * {@link LeilaoResponseDto}). Além disso, ela oferece métodos para converter listas 
 * de entidades em listas de DTOs. O mapeamento é feito utilizando a biblioteca ModelMapper.
 */
public class LeilaoMapper {

  /**
   * Converte um objeto {@link LeilaoForm} para uma entidade {@link Leilao}.
   *
   * @param createDto o objeto {@link LeilaoForm} a ser convertido.
   * @return a entidade {@link Leilao} correspondente.
   */
  public static Leilao toLeilao(LeilaoForm createDto) {
    return new ModelMapper().map(createDto, Leilao.class);
  }

  /**
   * Converte um objeto {@link Leilao} para um DTO {@link LeilaoResponseDto}.
   *
   * @param leilao a entidade {@link Leilao} a ser convertida.
   * @return o DTO {@link LeilaoResponseDto} correspondente.
   */
  public static LeilaoResponseDto toDto(Leilao leilao) {
    PropertyMap<Leilao, LeilaoResponseDto> props = new PropertyMap<Leilao, LeilaoResponseDto>() {
      @Override
      protected void configure() {
        map().setId(source.getId());
        map().setDescricao(source.getDescricao());
        map().setValorMinimo(source.getValorMinimo());
        map().setLeilaoStatus(source.getLeilaoStatus());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(leilao, LeilaoResponseDto.class);
  }

  /**
   * Converte uma lista de entidades {@link Leilao} para uma lista de DTOs {@link LeilaoResponseDto}.
   *
   * @param leilaos a lista de entidades {@link Leilao} a ser convertida.
   * @return a lista de DTOs {@link LeilaoResponseDto} correspondente.
   */
  public static List<LeilaoResponseDto> toListDto(List<Leilao> leilaos) {
    return leilaos.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }
}
