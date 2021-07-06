package com.rsimas.myfin.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rsimas.myfin.domain.Usuario;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarExistenciaDeEmail() {
		// Cenario
		Usuario user = criarUsuario();

		entityManager.persist(user);

		// Execução
		boolean result = repo.existsByEmail("Usuario@email.com");

		// Verifição
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoExistirUsuarioComOEmail() {
		// Cenario
		// DataJpaTest após o término da transação, efetua ROLLBACK.

		// Execução
		boolean result = repo.existsByEmail("Usuario@email.com");

		// Verifição
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void devePersistirUmUsuario() {
		// cenário
		Usuario usuario = criarUsuario();

		// Execução
		Usuario usuarioSalvo = repo.save(usuario);

		// Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		// execução
		Optional<Usuario> user = repo.findByEmail(usuario.getEmail());

		// verificação
		Assertions.assertThat(user.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioAoUsuarioPorEmailSeNaoHouverNaBase() {
		// execução
		Optional<Usuario> user = repo.findByEmail("Usuario@email.com");

		// verificação
		Assertions.assertThat(!user.isPresent()).isTrue();
	}

	public static Usuario criarUsuario() {
		return Usuario.builder().nome("Usuario").email("Usuario@email.com").senha("senha").build();
	}

}
