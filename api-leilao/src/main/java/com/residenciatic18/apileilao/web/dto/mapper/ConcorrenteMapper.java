package com.residenciatic18.apileilao.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.web.dto.ConcorrenteResponseDto;
import com.residenciatic18.apileilao.web.dto.form.ConcorrenteForm;

public class ConcorrenteMapper {

  public static Concorrente toConcorrente(ConcorrenteForm createDto) {
    return new ModelMapper().map(createDto, Concorrente.class);
  }

  public static ConcorrenteResponseDto toDto(Concorrente concorrente) {
    PropertyMap<Concorrente, ConcorrenteResponseDto> props = new PropertyMap<Concorrente, ConcorrenteResponseDto>() {
      @Override
      protected void configure() {
        map().setId(source.getId());
        map().setNome(source.getNome());
      }
    };
    ModelMapper mapper = new ModelMapper();
    mapper.addMappings(props);
    return mapper.map(concorrente, ConcorrenteResponseDto.class);
  }

  public static List<ConcorrenteResponseDto> toListDto(List<Concorrente> concorrentes) {
    return concorrentes.stream().map(user -> toDto(user)).collect(Collectors.toList());
  }
}
