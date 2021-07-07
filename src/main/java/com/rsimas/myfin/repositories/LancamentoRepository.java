package com.rsimas.myfin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rsimas.myfin.domain.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	
}
