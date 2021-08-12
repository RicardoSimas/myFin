package com.rsimas.myfin.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;
import com.rsimas.myfin.dto.AtualizaStatusDTO;
import com.rsimas.myfin.dto.LancamentoDTO;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.services.LancamentoService;
import com.rsimas.myfin.services.UsuarioService;

@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService service, UsuarioService userService) {
		this.service = service;
		this.usuarioService = userService;
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long id) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.buscarPorId(id);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Impossível realizar consulta. Usuario não encontrado na base!");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}	
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity buscarPorId(@PathVariable("id") Long id) {
		return service.buscarPorId(id)
				.map( lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity salvar(@RequestBody LancamentoDTO objDTO) {
		try {
			Lancamento newObj = fromDTO(objDTO);
			newObj = service.salvar(newObj);
			return new ResponseEntity(newObj, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO objDTO) {
		return service.buscarPorId(id).map(entity -> {
			try{
				Lancamento newObj = fromDTO(objDTO);
				newObj.setId(entity.getId());
				Lancamento objAtualizado = service.atualizar(newObj);
				return ResponseEntity.ok(objAtualizado);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		}).orElseGet( () ->
			new ResponseEntity("Lancamento não encontrado na base!", HttpStatus.BAD_REQUEST));
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{id}/atualiza-status", method = RequestMethod.PUT)
	public ResponseEntity atualizaStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO objDTO) {
		return service.buscarPorId(id).map(entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(objDTO.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Status não atualizado. Informe um status válido!");
			}
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch(RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
		new ResponseEntity("Lancamento não encontrado na base!", HttpStatus.BAD_REQUEST));	
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/greeting")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.buscarPorId(id).map(entity -> {
			service.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> 
			new ResponseEntity("Lancamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	public LancamentoDTO converter(Lancamento lancamento) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.Valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.Status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
	}
	
	public Lancamento fromDTO(LancamentoDTO objDTO) {
		Lancamento newobj = new Lancamento();
		newobj.setId(objDTO.getId());
		newobj.setDescricao(objDTO.getDescricao());
		newobj.setAno(objDTO.getAno());
		newobj.setMes(objDTO.getMes());
		newobj.setValor(objDTO.getValor());
		
		Usuario usuario = usuarioService.buscarPorId(objDTO.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o id informado!"));
		
		newobj.setUsuario(usuario);
		
		if(objDTO.getTipo() != null) {
			newobj.setTipo(TipoLancamento.valueOf(objDTO.getTipo()));
		}
		
		if(objDTO.getStatus() != null) {
			newobj.setStatus(StatusLancamento.valueOf(objDTO.getStatus()));
		}
		
		return newobj;
	}
}
