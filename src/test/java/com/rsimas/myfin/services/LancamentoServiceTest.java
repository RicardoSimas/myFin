package com.rsimas.myfin.services;

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
import com.rsimas.myfin.domain.enums.StatusLancamento;
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
		
		Assertions.assertThat( lancamento.getId()).isEqualTo(lancamentoSalvo.getId() );
		Assertions.assertThat( lancamentoSalvo.getStatus() ).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		Assertions.catchThrowable(() -> service.salvar(lancamentoASalvar));
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
}
