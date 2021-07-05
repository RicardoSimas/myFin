package com.rsimas.myfin.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Usuario;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repo;
		
	@org.junit.jupiter.api.Test
	public void deveVerificarExistenciaDeEmail() {
		//Cenario
		Usuario user = Usuario.builder()
				.nome("Usuario")
				.email("Usuario@email.com")
				.build();
		
		repo.save(user);
		
		//Execução
		boolean result = repo.existsByEmail("Usuario@email.com");
		
		//Verifição
		Assertions.assertThat(result).isTrue();
	}
	
	@org.junit.jupiter.api.Test
	public void deveRetornarFalsoQuandoNaoExistirUsuarioComOEmail() {
		//Cenario
		repo.deleteAll();
		
		//Execução
		boolean result = repo.existsByEmail("Usuario@email.com");
		
		//Verifição
		Assertions.assertThat(result).isFalse();
	}
}
