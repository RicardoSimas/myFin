package com.rsimas.myfin.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/testRest")
public class UsuarioResource {
	
	@RequestMapping(method=RequestMethod.GET)
	public String Listar() {
		return "Rest OK!";
	}
}
