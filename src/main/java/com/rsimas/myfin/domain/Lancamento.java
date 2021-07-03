package com.rsimas.myfin.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(schema = "financas")
public class Lancamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_lancamento")
	private Long id;
	
	@Column(name = "desc_lancamento")
	private String descricao;
	
	@Column(name = "ano_lancamento")
	private Integer ano;
	
	@Column(name = "mes_lancamento")
	private Integer mes;
	
	@Column(name = "valor_lancamento")
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "tipo_lancamento")
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "status_lancamento")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;
}
