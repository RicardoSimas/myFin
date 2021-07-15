package com.rsimas.myfin.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario(Long id);
}
