package com.produtoapi.controller;

import com.produtoapi.model.Produto;
import com.produtoapi.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Recebimento de requisições
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos") 
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return produtoService.atualizar(id, produto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }

    @GetMapping("/{id}")
    public Optional<Produto> findById(@PathVariable Long id) {
        return produtoService.findById(id);
    }
    
    @PostMapping("/salvarLista")
    public List<Produto> salvarLista(@RequestBody List<Produto> produtos){
    	return produtoService.salvarLista(produtos);
    }
    
	 // ==============================================================================
	 // =================== BUSCAS DETALHADAS POR NOME DE PRODUTO ====================
	 // ==============================================================================
	
	 // Endpoints de busca por nome

	 @GetMapping("/buscarPorNome")
	 public List<Produto> buscarPorNome(@RequestParam String valor) {
	     return produtoService.findByNome(valor);
	 }
	
	 @GetMapping("/buscarPorNomeContendo")
	 public List<Produto> buscarPorNomeContendo(@RequestParam String valor) {
	     return produtoService.findByNomeContaining(valor);
	 }
	
	 @GetMapping("/buscarPorNomeEStatus")
	 public List<Produto> buscarPorNomeEStatus(
	         @RequestParam String nome,
	         @RequestParam String status
	 ) {
	     return produtoService.findByNomeAndStatus(nome, status);
	 }
	
	 @GetMapping("/buscarPorNomeComecandoCom")
	 public List<Produto> buscarPorNomeComecandoCom(@RequestParam String valor) {
	     return produtoService.findByNomeStartingWith(valor);
	 }
	
	 @GetMapping("/buscarPorNomeTerminandoCom")
	 public List<Produto> buscarPorNomeTerminandoCom(@RequestParam String valor) {
	     return produtoService.findByNomeEndingWith(valor);
	 }
    
	// ==============================================================================
	// =================== BUSCAS DETALHADAS POR PREÇO ==============================
	// ==============================================================================

	@GetMapping("/buscarPorPreco")
	public List<Produto> buscarPorPreco(@RequestParam Double valor) {
	    return produtoService.findByPreco(valor);
	}

	@GetMapping("/buscarPorPrecoMaiorQue")
	public List<Produto> buscarPorPrecoMaiorQue(@RequestParam Double valor) {
	    return produtoService.findByPrecoGreaterThan(valor);
	}

	@GetMapping("/buscarPorPrecoMenorQue")
	public List<Produto> buscarPorPrecoMenorQue(@RequestParam Double valor) {
	    return produtoService.findByPrecoLessThan(valor);
	}

	@GetMapping("/buscarTotalPreco")
	public Double buscarTotalPreco() {
	    return produtoService.findTotalPreco();
	}
    
	// ==============================================================================
	// ================ BUSCAS DETALHADAS POR QUANTIDADE DE PRODUTO =================
	// ==============================================================================
	@GetMapping("/buscarPorQuantidade")
	public List<Produto> buscarPorQuantidade(@RequestParam Integer valor) {
		return produtoService.findByQuantidade(valor);
	}
	
	@GetMapping("/buscarPorQuantidadeMenorQue")
	public List<Produto> buscarPorQuantidadeMenorQue(@RequestParam Integer valor) {
		return produtoService.findByQuantidadeLessThan(valor);
	}
	
	@GetMapping("/buscarPorQuantidadeMaiorQue")
	public List<Produto> buscarPorQuantidadeMaiorQue(@RequestParam Integer valor) {
		return produtoService.findByQuantidadeGreaterThan(valor);
	}
	
	//==============================================================================
	// =================== BUSCAS DETALHADAS POR STATUS DE PRODUTO==================
	//==============================================================================
	@GetMapping("/buscarPorStatus")
	public List<Produto> buscarPorStatus(@RequestParam(required = false) String valor) {
	 return produtoService.findByStatus(valor);
	}
	
	// Retorna exclusivamente produtos com status null
	@GetMapping("/buscarPorStatusNulos")
	public List<Produto> buscarPorStatusNulos() {
	 return produtoService.findByStatusIsNull();
	}
	
	// Busca baseada em dois campos (preço e status)
	@GetMapping("/buscarPorPrecoEStatus")
	public List<Produto> buscarPorPrecoEStatus(@RequestParam Double preco,@RequestParam String status) {
	 return produtoService.findByPrecoAndStatus(preco, status);
	}
	
	// Endpoint para trazer o total de produtos
	@GetMapping("/contarTotalDeProdutos")
	public Long contarTotalDeProdutos() {
	 return produtoService.count();
	}
	
	// Se não passar nada, volta todos produtos com status padrão, neste caso
	@GetMapping("/buscarPorStatusPadrao")
	public List<Produto> buscarPorStatusPadrao(@RequestParam(defaultValue ="Disponível") String valor) {
	 return produtoService.findByStatus(valor);
	} 
	
}