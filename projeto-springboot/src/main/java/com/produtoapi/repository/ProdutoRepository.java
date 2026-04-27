package com.produtoapi.repository;

import com.produtoapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
	
	//busca por nome
	List<Produto> findByNome(String nome);
	List<Produto> findByNomeContaining(String nome);
	List<Produto> findByNomeAndStatus(String nome, String status);
	List<Produto> findByNomeStartingWith(String prefix);
	List<Produto> findByNomeEndingWith(String suffix);
	
	// Busca por preço
	List<Produto> findByPreco(Double preco);
	List<Produto> findByPrecoGreaterThan(Double preco);
	List<Produto> findByPrecoLessThan(Double preco);
	
	// Usando anottation @Query para obter total de preços
	@Query("SELECT SUM(p.preco) FROM Produto p")
	Double findTotalPreco();
	
	// Buscas por quantidade de produto
	List<Produto> findByQuantidade(Integer quantidade);
	List<Produto> findByQuantidadeLessThan(Integer quantidade);
	List<Produto> findByQuantidadeGreaterThan(Integer quantidade);
	
	// Buscas por preço e status
	List<Produto> findByStatus(String status);
	List<Produto> findByStatusIsNull();
	List<Produto> findByPrecoAndStatus(Double preco, String status); 
	
}