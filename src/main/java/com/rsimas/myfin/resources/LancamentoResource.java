package com.rsimas.myfin.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.dto.LancamentoDTO;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.services.LancamentoService;
import com.rsimas.myfin.services.UsuarioService;

@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {

	private LancamentoService service;
	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.buscarPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Impossível realizar consulta. Usuario não encontrado na base!");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
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

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO objDTO) {
		return service.buscarPorId(id).map(entity -> {
			try{
				Lancamento newObj = service.fromDTO(objDTO);
				newObj.setId(entity.getId());
				Lancamento objAtualizado = service.Atualizar(newObj);
				return ResponseEntity.ok(objAtualizado);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		}).orElseGet( () ->
			new ResponseEntity("Lancamento não encontrado na base!", HttpStatus.BAD_REQUEST));
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.buscarPorId(id).map(entity -> {
			service.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> 
			new ResponseEntity("Lancamento não encontrado", HttpStatus.BAD_REQUEST));
	}
}
