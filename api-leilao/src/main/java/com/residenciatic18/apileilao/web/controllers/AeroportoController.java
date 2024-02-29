package com.residenciatic18.apileilao.web.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.residenciatic18.apileilao.entities.Aeroporto;
import com.residenciatic18.apileilao.services.AeroportoService;
import com.residenciatic18.apileilao.web.dto.AeroportoResponseDto;
import com.residenciatic18.apileilao.web.dto.form.AeroportoForm;
import com.residenciatic18.apileilao.web.dto.mapper.AeroportoMapper;

@RestController
@RequestMapping("/aeroportos")
public class AeroportoController {

    @Autowired
    private AeroportoService aeroportoService;

    @PostMapping("/create")
    public ResponseEntity<AeroportoResponseDto> create(@RequestBody AeroportoForm createDto) {
        Aeroporto obj = aeroportoService.salvar(AeroportoMapper.toAeroporto(createDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(AeroportoMapper.toDto(obj));
    }

    @GetMapping("/listar-todos/")
    public ResponseEntity<List<AeroportoResponseDto>> listarAeroportos(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String icao) {
        return ResponseEntity.ok(AeroportoMapper.toListDto(aeroportoService.buscarPorNomeOuIcao(nome, icao)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AeroportoResponseDto> update(@PathVariable Long id, @RequestBody AeroportoForm createDto) {
        try {
            return ResponseEntity.ok(AeroportoMapper.toDto(aeroportoService.update(id, createDto)));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {

        if (aeroportoService.isExisteId(id)) {

            try {
                aeroportoService.delete(id);
                return ResponseEntity.ok().build();

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}