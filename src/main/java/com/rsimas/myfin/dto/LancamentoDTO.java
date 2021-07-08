package com.rsimas.myfin.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {
	
	private Long id;
	
	private String descricao;
	
	private Integer mes;
	
	private Integer ano;
	
	private BigDecimal Valor;
	
	private Long usuario;
	
	private String tipo;
	
	private String Status;
	
}
