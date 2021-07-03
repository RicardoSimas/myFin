package com.rsimas.myfin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rsimas.myfin.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	boolean existsByEmail(String email);
}
