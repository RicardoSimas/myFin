package com.rsimas.myfin.services;

import java.util.List;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.dto.LancamentoDTO;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento Atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Lancamento fromDTO(LancamentoDTO objDTO);
}
