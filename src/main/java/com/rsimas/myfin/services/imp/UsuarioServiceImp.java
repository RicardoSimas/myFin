package com.rsimas.myfin.services.imp;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.exceptions.ErroAutenticacao;
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

	@Transactional
	public Usuario salvar(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado!");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha informada é inválida!");
		}
		
		return usuario.get();
		
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}
	
	
}
