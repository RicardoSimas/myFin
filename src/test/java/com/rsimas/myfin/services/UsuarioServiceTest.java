package com.rsimas.myfin.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.repositories.UsuarioRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow( () -> { 
			//cenário
			repository.deleteAll();
			
			//execução
			service.validarEmail("teste@email.com");		
		});
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, ()-> {
			//cenário
			Usuario usuario = Usuario.builder().nome("teste").email("teste@email.com").build();
			repository.save(usuario);
			
			//execução
			service.validarEmail("teste@email.com");			
		});
	}
}
