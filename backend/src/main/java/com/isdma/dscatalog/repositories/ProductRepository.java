package com.isdma.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	//COALESCE, adaptação ao valor null para mais bancos de dados perceberem
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE " //Se nao metermos distinct pode haver repetição de produtos quandos buscamos todos porque podem pertencer a mai que uma categoria ja que se trata de muitos para muitos
			+ "(COALESCE(:categories) IS NULL OR cats IN :categories) AND " //se for null nem testa o resto faz a query sem a condição where se nao for entao vai para a segunda parte do ou e coloca a condição na query jpql
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%')))")  //name igualzinho ao que ta na classe sempre assim, % para ir buscar a palavra no meio do texto se tiver dos 2 lados, mas como o % leva '' aqui temos de concatenar entao usamos o CONCAT que fica mais facil, LOWER é para converter ambos as palavras para minuscula e so depois comparar porque senao se espeve-se gamer por exemplo, e o nome fosse Gamer, nao encontra
	Page<Product> find(List<Category> categories, String name, Pageable pageable);
}
