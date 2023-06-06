package br.com.cotiinformatica.controllers;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.dtos.ContasEdicaoDto;
import br.com.cotiinformatica.entities.Conta;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.repositories.CategoriaRepository;
import br.com.cotiinformatica.repositories.ContaRepository;

@Controller
public class ContasEdicaoController {

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//Método que abre a página de edição
	@RequestMapping(value = "/admin/contas-edicao")
	public ModelAndView contasEdicao(Integer idConta, HttpServletRequest request) {
		
		// WEB-INF/views/admin/contas-edicao.jsp
		ModelAndView modelAndView = new ModelAndView("admin/contas-edicao");
		
		try {
			//capturando o usuário armazenado na sessão
			Usuario usuario = (Usuario) request.getSession().getAttribute("auth_usuario");
			
			//buscar no banco de dados a conta através do ID
			Conta conta = contaRepository.findById(idConta, usuario.getIdUsuario());
			
			//criando um DTO com os dados da conta para edição
			ContasEdicaoDto dto = new ContasEdicaoDto();
			dto.setIdConta(conta.getIdConta());
			dto.setNome(conta.getNome());
			dto.setData(new SimpleDateFormat("yyyy-MM-dd").format(conta.getData()));
			dto.setValor(conta.getValor().toString());
			dto.setObservacoes(conta.getObservacoes());
			dto.setIdCategoria(conta.getCategoria().getIdCategoria());
			
			//enviando os dados para a página
			modelAndView.addObject("dto", dto);
			modelAndView.addObject("categorias", categoriaRepository.findAll(usuario.getIdUsuario()));
		}
		catch(Exception e) {
			modelAndView.addObject("mensagem", e.getMessage());
		}
		
		return modelAndView;
	}
	
	//Método para receber o SUBMIT POST do formulário de edição
	@RequestMapping(value = "/admin/contas-edicao-post", method = RequestMethod.POST)
	public ModelAndView contasEdicaoPost(ContasEdicaoDto dto, HttpServletRequest request) {
		
		// WEB-INF/views/admin/contas-edicao.jsp
		ModelAndView modelAndView = new ModelAndView("admin/contas-edicao");
		
		try {
			//capturando o usuário armazenado na sessão
			Usuario usuario = (Usuario) request.getSession().getAttribute("auth_usuario");
			
			//capturando os dados da conta
			Conta conta = new Conta();
			conta.setIdConta(dto.getIdConta());
			conta.setNome(dto.getNome());
			conta.setData(new SimpleDateFormat("yyyy-MM-dd").parse(dto.getData()));
			conta.setValor(Double.parseDouble(dto.getValor()));
			conta.setObservacoes(dto.getObservacoes());
			conta.setIdCategoria(dto.getIdCategoria());
			conta.setIdUsuario(usuario.getIdUsuario());
			
			contaRepository.update(conta); //atualizando no banco de dados
			
			modelAndView.addObject("mensagem", "Conta atualizada com sucesso.");
			modelAndView.addObject("dto", dto);
			modelAndView.addObject("categorias", categoriaRepository.findAll(usuario.getIdUsuario()));
		}
		catch(Exception e) {
			modelAndView.addObject("mensagem", e.getMessage());
		}
		
		return modelAndView;
	}	
	
}











