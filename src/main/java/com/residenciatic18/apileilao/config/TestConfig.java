package com.residenciatic18.apileilao.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.residenciatic18.apileilao.entities.Concorrente;
import com.residenciatic18.apileilao.entities.Lance;
import com.residenciatic18.apileilao.entities.Leilao;
import com.residenciatic18.apileilao.entities.enums.LeilaoStatus;
import com.residenciatic18.apileilao.repositories.ConcorrenteRepository;
import com.residenciatic18.apileilao.repositories.LanceRepository;
import com.residenciatic18.apileilao.repositories.LeilaoRepository;

/**
 * Classe de configuração utilizada para popular o banco de dados com dados de teste
 * quando o perfil "test" estiver ativo.
 * 
 * Esta classe é executada automaticamente ao iniciar a aplicação com o perfil "test",
 * e insere dados fictícios nas tabelas de Leilão, Concorrente e Lance para facilitar os testes.
 */
@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	/**
	 * Repositório de acesso à entidade Leilao.
	 */
	@Autowired
	private LeilaoRepository leilaoRepository;

	/**
	 * Repositório de acesso à entidade Concorrente.
	 */
	@Autowired
	private ConcorrenteRepository concorrenteRepository;

	/**
	 * Repositório de acesso à entidade Lance.
	 */
	@Autowired
	private LanceRepository lanceRepository;

	/**
	 * Método executado ao iniciar a aplicação, responsável por inserir dados
	 * iniciais no banco de dados para testes.
	 * 
	 * @param args Argumentos da linha de comando.
	 * @throws Exception Se ocorrer algum erro durante a execução.
	 */
	@Override
	public void run(String... args) throws Exception {

		// Criação de concorrentes fictícios
		Concorrente c1 = new Concorrente("João", "123.456.789-00");
		Concorrente c2 = new Concorrente("Maria", "987.654.321-89");
		Concorrente c3 = new Concorrente("José", "123.123.123-34");
		Concorrente c4 = new Concorrente("Ana", "456.456.456-45");
		Concorrente c5 = new Concorrente("Carlos", "789.789.789-78");
		Concorrente c6 = new Concorrente("Paula", "668.354.431-32");

		// Criação de leilões fictícios com status variados
		Leilao l1 = new Leilao("Leilao de um carro", 10000.00, LeilaoStatus.ABERTO);
		Leilao l2 = new Leilao("Leilao de um computador", 5000.00, LeilaoStatus.ABERTO);
		Leilao l3 = new Leilao("Leilao de um celular", 3000.00, LeilaoStatus.FECHADO);
		Leilao l4 = new Leilao("Leilao de um livro", 1000.00, LeilaoStatus.FECHADO);
		Leilao l5 = new Leilao("Leilao de um quadro", 2000.00, LeilaoStatus.ABERTO);
		Leilao l6 = new Leilao("Leilao de um notebook", 4000.00, LeilaoStatus.ABERTO);

		// Salvando leilões e concorrentes no banco
		leilaoRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6));
		concorrenteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5, c6));

		// Criação de lances fictícios entre concorrentes e leilões
		Lance lance1 = new Lance(l1, c1, 8000.00);
		Lance lance2 = new Lance(l2, c2, 6000.00);
		Lance lance3 = new Lance(l6, c3, 4000.00);
		Lance lance4 = new Lance(l4, c4, 2000.00);
		Lance lance5 = new Lance(l5, c3, 3000.00);
		Lance lance6 = new Lance(l6, c6, 5000.00);
		Lance lance7 = new Lance(l1, c5, 9000.00);
		Lance lance8 = new Lance(l2, c1, 7000.00);

		// Salvando lances no banco (inclui um lance duplicado: lance1)
		lanceRepository.saveAll(Arrays.asList(lance1, lance2, lance3, lance4, lance5, lance6, lance1, lance7, lance8));
	}
}
