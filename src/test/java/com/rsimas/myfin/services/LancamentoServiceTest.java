package com.rsimas.myfin.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.repositories.LancamentoRepository;
import com.rsimas.myfin.repositories.LancamentoRepositoryTest;
import com.rsimas.myfin.services.imp.LancamentoServiceImp;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImp service;

	@MockBean
	LancamentoRepository repository;

	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

		Lancamento lancamento = service.salvar(lancamentoASalvar);

		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamentoSalvo.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}

	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

		Assertions.catchThrowable(() -> service.salvar(lancamentoASalvar));

		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);

		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

		Lancamento lancamento = service.atualizar(lancamentoSalvo);

		Mockito.verify(repository, Mockito.times(1)).save(lancamento);
	}

	@Test
	public void naoDeveAtualizaUmLancamentoQueNaoEstaNaBase() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);

		Mockito.verify(repository, Mockito.never()).save(lancamento);
	}

	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);

		service.deletar(lancamentoSalvo);

		Mockito.verify(repository).delete(lancamentoSalvo);
	}

	@Test
	public void naoDeveDeletarUmLancamentoQueNaoEstaNaBase() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);

		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);

		List<Lancamento> lista = java.util.Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(org.springframework.data.domain.Example.class))).thenReturn(lista);

		List<Lancamento> result = service.buscar(lancamento);

		Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(lancamento);
	}

	@Test
	public void deveAlterarStatusDeUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);

		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

		service.atualizarStatus(lancamento, novoStatus);

		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service, Mockito.times(1)).atualizar(lancamento);
	}

	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);

		Mockito.when(repository.findById(lancamento.getId())).thenReturn(Optional.of(lancamento));

		Optional<Lancamento> objRetornado = service.buscarPorId(lancamento.getId());

		Assertions.assertThat(lancamento.getId()).isEqualTo(objRetornado.get().getId());
	}

	@Test
	public void deveRetornarVazioQuandoBuscarUmLancamentoInexistentePorId() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

		Mockito.when(repository.findById(lancamento.getId())).thenReturn(Optional.empty());

		Optional<Lancamento> objRetornado = service.buscarPorId(lancamento.getId());

		Assertions.assertThat(objRetornado.isPresent()).isFalse();
	}

	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		Usuario user = new Usuario();

		Throwable exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe uma Descrição válida.");
		
		lancamento.setDescricao(" ");
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe uma Descrição válida.");

		lancamento.setDescricao("Descrição");
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(0);
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(13);
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Mês válido.");

		lancamento.setMes(2);

		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(21);
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Ano válido.");

		lancamento.setAno(2019);
		
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Usuário.");
		
		user.setId(null);
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Usuário.");
		
		user.setId(1l);
		lancamento.setUsuario(user);
		
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.ZERO);
		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um Valor válido.");

		lancamento.setValor(BigDecimal.valueOf(1500));

		exceptionCapturada = Assertions.catchThrowable(() -> service.validar(lancamento));

		Assertions.assertThat(exceptionCapturada).isInstanceOf(RegraNegocioException.class)
				.hasMessage("Informe um tipo de Lançamento.");

		lancamento.setTipo(TipoLancamento.RECEITA);

	}
}
