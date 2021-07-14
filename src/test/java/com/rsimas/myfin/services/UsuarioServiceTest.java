package com.rsimas.myfin.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	@SpyBean
	UsuarioServiceImp service;
	
	@MockBean
	UsuarioRepository repository;
	
	public static String email = "email@teste.com";
	public static String senha = "senha";
	
	@Test
	public void deveSalvarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Usuario user = Usuario.builder()
					.id(1l)
					.email("email@test.com")
					.nome("usuario")
					.senha("senha")
					.build();
			
			Mockito.doNothing().when(service).validarEmail(user.getEmail());
			Mockito.when( repository.save(Mockito.any(Usuario.class))).thenReturn(user);
			
			//execução
			Usuario userSave = service.salvar(user);
			
			//verificação
			org.assertj.core.api.Assertions.assertThat(userSave).isNotNull();
			org.assertj.core.api.Assertions.assertThat(userSave.getId()).isEqualTo(user.getId());
			org.assertj.core.api.Assertions.assertThat(userSave.getEmail()).isEqualTo(user.getEmail());
			org.assertj.core.api.Assertions.assertThat(userSave.getNome()).isEqualTo(user.getNome());
			org.assertj.core.api.Assertions.assertThat(userSave.getSenha()).isEqualTo(user.getSenha());
		});
	}
	
	@Test
	public void deveDarErroDeValidacaoDeEmailAoTentarSalvarUsuario() {
		Assertions.assertThrows(ErroAutenticacao.class, ()-> {
			//cenário
			Usuario user = criarUsuario();
			
			Mockito.doThrow(ErroAutenticacao.class).when(service).validarEmail(user.getEmail());
			
			//execução
			service.salvar(user);
			
			//verificação
			Mockito.verify( repository, Mockito.never() ).save(user);
		});
	}
	
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
			//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//execução
			Throwable exceptionCapturada = org.assertj.core.api.Assertions
					.catchThrowable( ()-> service.autenticar(email, senha));
			
			//Verificação
			org.assertj.core.api.Assertions.assertThat(exceptionCapturada)
					.isInstanceOf(ErroAutenticacao.class)
					.hasMessage("Usuario não encontrado!");
	}
	
	@Test
	public void deveLancarErroDeAutenticacaoParaSenhaInvalida() {
			//cenário
			Usuario user = criarUsuario();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
			
			//execução
			Throwable exceptionCapturada = org.assertj.core.api.Assertions
					.catchThrowable( () -> service.autenticar(email, "123"));
			
			//Verificação
			org.assertj.core.api.Assertions.assertThat(exceptionCapturada)
					.isInstanceOf(ErroAutenticacao.class)
					.hasMessage("Senha informada é inválida!");
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
