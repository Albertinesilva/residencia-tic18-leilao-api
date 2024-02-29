package com.residenciatic18.apileilao.services;

import java.util.List;

import com.residenciatic18.apileilao.entities.Aeroporto;
import com.residenciatic18.apileilao.web.dto.form.AeroportoForm;

public interface AeroportoService {

    List<Aeroporto> buscarPorNomeOuIcao(String nome, String icao);

    Aeroporto salvar(Aeroporto aeroporto);

    Aeroporto buscarPorId(Long id);

    Aeroporto update(Long id, AeroportoForm aeroportoForm);

    void delete(Long id);

    Boolean isExisteId(Long id);

}