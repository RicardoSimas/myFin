package com.rsimas.myfin.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	@Query(value = 
			  " SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u"
			+ " WHERE u.id =:idUsuario AND l.tipo =:tipo and l.status =:status GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status); 
}
