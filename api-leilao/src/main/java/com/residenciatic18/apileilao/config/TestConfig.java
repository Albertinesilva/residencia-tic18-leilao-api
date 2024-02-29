package com.residenciatic18.apileilao.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private LeilaoRepository leilaoRepository;

	@Autowired
	private ConcorrenteRepository concorrenteRepository;

	@SuppressWarnings("null")
  @Override
	public void run(String... args) throws Exception {

		Leilao l1 = new Leilao("Leilao de um carro", 10000.00, LeilaoStatus.ABERTO);
    Leilao l2 = new Leilao("Leilao de um computador", 5000.00, LeilaoStatus.ABERTO);
    Leilao l3 = new Leilao("Leilao de um celular", 3000.00, LeilaoStatus.FECHADO);
    Leilao l4 = new Leilao("Leilao de um livro", 1000.00, LeilaoStatus.FECHADO);
    Leilao l5 = new Leilao("Leilao de um quadro", 2000.00, LeilaoStatus.ABERTO);

		leilaoRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5));

		Concorrente c1 = new Concorrente("João", "12345678900");
		Concorrente c2 = new Concorrente("Maria", "98765432100");
		Concorrente c3 = new Concorrente("José", "12312312300");
		Concorrente c4 = new Concorrente("Ana", "45645645600");
		Concorrente c5 = new Concorrente("Carlos", "78978978900");

		concorrenteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
	
	}
}