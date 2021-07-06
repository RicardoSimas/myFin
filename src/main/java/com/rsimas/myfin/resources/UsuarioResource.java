package com.rsimas.myfin.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.dto.UsuarioDTO;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.services.UsuarioService;

@RestController
@RequestMapping(value="/api/usuarios")
public class UsuarioResource {
	
	@Autowired
	UsuarioService service;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto) {
		
		Usuario obj = service.fromDTO(dto);
		
		try {
			Usuario usuarioSalvo = service.salvar(obj);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
