package com.rsimas.myfin.services.imp;

import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		lancamento.setId(null);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento Atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		Atualizar(lancamento);
	}
}
