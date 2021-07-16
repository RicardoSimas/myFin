package com.rsimas.myfin.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.dto.UsuarioDTO;
import com.rsimas.myfin.exceptions.ErroAutenticacao;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.services.LancamentoService;
import com.rsimas.myfin.services.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {
	
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception{
		//Cenário
		String email = "usuario@email.com";
		String senha = "senha";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario user = Usuario.builder().id(1l).email(email).senha(senha).build();
		Mockito.when(service.autenticar(email, senha)).thenReturn(user);
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isOk())
			.andExpect( MockMvcResultMatchers.jsonPath("id").value(user.getId()) )
			.andExpect( MockMvcResultMatchers.jsonPath("email").value(user.getEmail()) )
		;
	}
	
	@Test
	public void deveRetornarBadRequestPorErroDeAutenticacao() throws Exception{
		//Cenário
		String email = "usuario@email.com";
		String senha = "senha";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isBadRequest() )
		;
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception{
		//Cenário
		String email = "usuario@email.com";
		String senha = "senha";
		
		UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("senha").build();
		Usuario user = Usuario.builder().id(1l).email(email).senha(senha).build();
		Mockito.when(service.salvar(Mockito.any(Usuario.class))).thenReturn(user);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isCreated())
		;
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception{
		
		Mockito.when(service.salvar(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		//Execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON);
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isBadRequest());
	}
}
