package com.rsimas.myfin.services.imp;

import java.util.List;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.repositories.LancamentoRepository;
import com.rsimas.myfin.services.LancamentoService;

public class LancamentoServiceImp implements LancamentoService{
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImp(LancamentoRepository repo) {
		this.repository = repo;
	}

	@Override
	public Lancamento salvar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Lancamento Atualizar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		// TODO Auto-generated method stub
		
	}

}
