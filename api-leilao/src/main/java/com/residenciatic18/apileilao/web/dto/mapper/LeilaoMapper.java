package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.web.dto.LeilaoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.LeilaoForm;

public class LeilaoMapper {

  public static Leilao toLeilao(LeilaoForm createDto) {
    return new ModelMapper().map(createDto, Leilao.class);
  }

  public static LeilaoResponseDto toDto(Leilao leilao) {
    PropertyMap<Leilao, LeilaoResponseDto> props = new PropertyMap<Leilao, LeilaoResponseDto>() {
      @Override
      protected void configure() {
        map().setId(source.getId());
        map().setDescricrao(source.getDescricrao());
        map().setValorMinimo(source.getValorMinimo());
        map().setLeilaoStatus(source.getLeilaoStatus());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(leilao, LeilaoResponseDto.class);
  }

  public static List<LeilaoResponseDto> toListDto(List<Leilao> leilaos) {
    return leilaos.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }
}