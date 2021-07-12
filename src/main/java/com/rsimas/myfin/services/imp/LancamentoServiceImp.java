package com.rsimas.myfin.services.imp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsimas.myfin.domain.Lancamento;
import com.rsimas.myfin.domain.Usuario;
import com.rsimas.myfin.domain.enums.StatusLancamento;
import com.rsimas.myfin.domain.enums.TipoLancamento;
import com.rsimas.myfin.dto.LancamentoDTO;
import com.rsimas.myfin.exceptions.RegraNegocioException;
import com.rsimas.myfin.repositories.LancamentoRepository;
import com.rsimas.myfin.services.LancamentoService;
import com.rsimas.myfin.services.UsuarioService;

@Service
public class LancamentoServiceImp implements LancamentoService {

	private LancamentoRepository repository;
	private UsuarioService userService;

	public LancamentoServiceImp(LancamentoRepository repo, UsuarioService userService) {
		this.repository = repo;
		this.userService = userService;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento Atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example<Lancamento> filtroBusca = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

		return repository.findAll(filtroBusca);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		Atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {

		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}

		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}

		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}

		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}

		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}

		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lançamento.");
		}
	}

	@Override
	public Lancamento fromDTO(LancamentoDTO objDTO) {
		Lancamento newobj = new Lancamento();
		newobj.setId(objDTO.getId());
		newobj.setDescricao(objDTO.getDescricao());
		newobj.setAno(objDTO.getAno());
		newobj.setMes(objDTO.getMes());
		newobj.setValor(objDTO.getValor());
		
		Usuario usuario = userService.buscarPorId(objDTO.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o id informado!"));
		
		newobj.setUsuario(usuario);
		
		if(objDTO.getTipo() != null) {
			newobj.setTipo(TipoLancamento.valueOf(objDTO.getTipo()));
		}
		
		if(objDTO.getStatus() != null) {
			newobj.setStatus(StatusLancamento.valueOf(objDTO.getStatus()));
		}
		
		return newobj;
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		return repository.findById(id);
	}
}
