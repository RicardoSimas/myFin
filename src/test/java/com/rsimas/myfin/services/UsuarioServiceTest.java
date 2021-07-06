package com.rsimas.myfin.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.exceptions.ErroAutenticacao;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.repositories.UsuarioRepository;
import com.rsimas.myfin.services.imp.UsuarioServiceImp;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {
	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@BeforeEach
	public void Setup() {
		service = new UsuarioServiceImp(repository);
	}
	
	public static String email = "email@teste.com";
	public static String senha = "senha";
		
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow( () -> {
			//cenário
			Usuario user = criarUsuario();
			
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));
			
			//execução
			Usuario autenticado = service.autenticar(email, senha);
			
			//verificação
			org.assertj.core.api.Assertions.assertThat(autenticado).isNotNull();
		});
	}
	
	@Test
	public void deveLancarErroDeAutenticacaoParaUsuarioNaoEncontradoNaBase() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//execução
			Throwable exceptionCapturada = org.assertj.core.api.Assertions
					.catchThrowable( ()-> service.autenticar(email, senha));
			
			//Verificação
			org.assertj.core.api.Assertions.assertThat(exceptionCapturada)
					.isInstanceOf(ErroAutenticacao.class)
					.hasMessage("Usuario não encontrado!");
			
		});
	}
	
	@Test
	public void deveLancarErroDeAutenticacaoParaSenhaInvalida() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			//cenário
			Usuario user = criarUsuario();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
			
			//execução
			Throwable exceptionCapturada = org.assertj.core.api.Assertions
					.catchThrowable( () -> service.autenticar(email, "123"));
			
			//Verificação
			org.assertj.core.api.Assertions.assertThat(exceptionCapturada)
					.isInstanceOf(ErroAutenticacao.class)
					.hasMessage("Senha inválida");
		});
	}
	
	@Test
	public void deveValidarEmail() {		
		Assertions.assertDoesNotThrow( () -> { 
			//cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			
			//execução
			service.validarEmail("teste@email.com");		
		});
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, ()-> {
			//cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			//execução
			service.validarEmail("teste@email.com");			
		});
	}
	
	public static Usuario criarUsuario() {
		return Usuario.builder().nome("Usuario").email("Usuario@email.com").senha("senha").build();
	}

}
