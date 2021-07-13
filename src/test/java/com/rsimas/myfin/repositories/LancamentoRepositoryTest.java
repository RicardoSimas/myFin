package com.rsimas.myfin.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
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
		Lancamento lancamento = criarEPersistirLancamento();

		Lancamento lancamentoPersistido = entityManager.find(Lancamento.class, lancamento.getId());

		lancamentoRepository.delete(lancamentoPersistido);

		Lancamento lancamentoDeletado = entityManager.find(Lancamento.class, lancamento.getId());

		Assertions.assertThat(lancamentoDeletado).isNull();
	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirLancamento();

		lancamento.setDescricao("Pagamento carro");
		lancamento.setStatus(StatusLancamento.CANCELADO);

		lancamentoRepository.save(lancamento);

		Lancamento lancamentoUpdate = entityManager.find(Lancamento.class, lancamento.getId());

		Assertions.assertThat(lancamentoUpdate.getDescricao()).isEqualTo("Pagamento carro");
		Assertions.assertThat(lancamentoUpdate.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirLancamento();
		
		Optional<Lancamento> findLancamento = lancamentoRepository.findById(lancamento.getId());
		
		Assertions.assertThat(findLancamento.isPresent()).isTrue();
	}

	private Lancamento criarEPersistirLancamento() {
		Lancamento lancamento = criarLancamento();

		entityManager.persist(lancamento);

		return lancamento;
	}

	private Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder().ano(2019).mes(1).descricao("Lancamento")
				.valor(BigDecimal.valueOf(10)).tipo(TipoLancamento.DESPESA).status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now()).build();

		return lancamento;
	}
}
