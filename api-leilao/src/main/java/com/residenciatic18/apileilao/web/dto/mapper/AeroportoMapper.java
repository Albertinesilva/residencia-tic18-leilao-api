package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Aeroporto;
import com.residenciatic18.apileilao.web.dto.AeroportoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.AeroportoForm;

public class AeroportoMapper {

  public static Aeroporto toAeroporto(AeroportoForm createDto) {
    return new ModelMapper().map(createDto, Aeroporto.class);
  }

  public static AeroportoResponseDto toDto(Aeroporto aeroporto) {
    PropertyMap<Aeroporto, AeroportoResponseDto> props = new PropertyMap<Aeroporto, AeroportoResponseDto>() {
      @Override
      protected void configure() {
        map().setId(source.getId());
        map().setIcao(source.getIcao());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(aeroporto, AeroportoResponseDto.class);
  }

  public static List<AeroportoResponseDto> toListDto(List<Aeroporto> aeroportos) {
    return aeroportos.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }

}
