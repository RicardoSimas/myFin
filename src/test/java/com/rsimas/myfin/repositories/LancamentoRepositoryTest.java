package com.rsimas.myfin.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Test
	public void devePersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		Assertions.assertThat(lancamentoSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		
		entityManager.persist(lancamento);
		
		Lancamento lancamentoPersistido = entityManager.find(Lancamento.class, lancamento.getId());
		
		lancamentoRepository.delete(lancamentoPersistido);
		
		Lancamento lancamentoDeletado = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoDeletado).isNull();
	}
	
	public Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder()
				.ano(2019)
				.mes(1)
				.descricao("Lancamento")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.DESPESA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
		
		return lancamento;
	}
}
