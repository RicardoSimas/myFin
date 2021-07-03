package com.rsimas.myfin.services.imp;

import org.springframework.stereotype.Service;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.repositories.UsuarioRepository;
import com.rsimas.myfin.services.UsuarioService;

@Service
public class UsuarioServiceImp implements UsuarioService{
	
	private UsuarioRepository repository;
	
	public UsuarioServiceImp(UsuarioRepository repo) {
		super();
		this.repository = repo;
	}

	@Override
	public Usuario salvar(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}
	
	
}
