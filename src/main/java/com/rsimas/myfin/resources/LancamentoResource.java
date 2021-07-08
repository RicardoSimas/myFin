package com.rsimas.myfin.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.dto.LancamentoDTO;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.services.LancamentoService;

@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {

	private LancamentoService service;

	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvar(@RequestBody LancamentoDTO objDTO) {
		try {
			Lancamento newObj = service.fromDTO(objDTO);
			return new ResponseEntity(newObj, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO objDTO) {
		return service.buscarPorId(id).map(entity -> {
			Lancamento newObj = service.fromDTO(objDTO);
			newObj.setId(entity.getId());
			Lancamento objAtualizado = service.Atualizar(newObj);

			return ResponseEntity.ok(objAtualizado);
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base!", HttpStatus.BAD_REQUEST));
	}
}
