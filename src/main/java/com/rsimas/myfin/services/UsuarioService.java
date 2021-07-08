package com.rsimas.myfin.services;

import java.util.Optional;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.dto.UsuarioDTO;

public interface UsuarioService {
	
	Usuario salvar(Usuario usuario);
	
	Usuario autenticar(String email, String senha);
	
	void validarEmail(String email);
	
	Usuario fromDTO(UsuarioDTO objDTO);
	
	Optional<Usuario> buscarPorId(Long id);
}
