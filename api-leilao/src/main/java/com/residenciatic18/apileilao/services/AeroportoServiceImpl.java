package com.residenciatic18.apileilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.residenciatic18.apileilao.entities.Aeroporto;
import com.residenciatic18.apileilao.repositories.AeroportoRepository;
import com.residenciatic18.apileilao.web.dto.form.AeroportoForm;

@Service
@Transactional(readOnly = false)
public class AeroportoServiceImpl implements AeroportoService {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @SuppressWarnings("null")
    @Override
    @Transactional
    public Aeroporto salvar(Aeroporto aeroporto) {
        return aeroportoRepository.save(aeroporto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aeroporto> buscarPorNomeOuIcao(String nome, String icao) {

        List<Aeroporto> aeroportos;

        if (nome != null && !nome.isEmpty() && icao != null && !icao.isEmpty()) {
            aeroportos = aeroportoRepository.findByNomeAndIcao(nome, icao);

        } else if (nome != null && !nome.isEmpty()) {
            aeroportos = aeroportoRepository.findByNome(nome);

        } else if (icao != null && !icao.isEmpty()) {
            aeroportos = aeroportoRepository.findByIcao(icao);

        } else {
            aeroportos = aeroportoRepository.findAll();
        }

        return aeroportos;
    }

    @Override
    public Aeroporto buscarPorId(Long id) {
        return aeroportoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Inválido para condutor:" + id));
    }

    @Override
    public Aeroporto update(Long id, AeroportoForm aeroportoForm) {
        Aeroporto aeroporto = buscarPorId(id);
        aeroporto.setNome(aeroportoForm.getNome());
        aeroporto.setIcao(aeroportoForm.getIcao());
        return salvar(aeroporto);
    }

    @Override
    public void delete(Long id) {
        aeroportoRepository.deleteById(id);
    }

    @Override
    public Boolean isExisteId(Long id) {
        if (aeroportoRepository.existsById(id)) {
            return true;
        } else {
            return false;
        }
    }
}