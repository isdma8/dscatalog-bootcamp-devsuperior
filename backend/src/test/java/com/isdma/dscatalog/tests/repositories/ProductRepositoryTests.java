package com.isdma.dscatalog.tests.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.isdma.dscatalog.entities.Product;
import com.isdma.dscatalog.repositories.ProductRepository;
import com.isdma.dscatalog.tests.factory.ProductFactory;

@DataJpaTest //so vai carregar os componentes necessarios para rodar o teste, nao vai ficar chamndo o contexto da aplicação servidor web, como é um repository so precisa destes componentes
//ele faz tambem com que cada 1 dos tests pegue a bd do h2 sempre na forma como estava antes de qualquer test, ele faz o test e faz rollback dos dados ao incial
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonexistingId;
	private Long countTotalProducts;
	private Long countPCGamerProducts;
	private PageRequest pageRequest;
	
	@BeforeEach //antes de fazer quanquer teste colocar o id como 1
	void setUp() throws Exception {
		existingId = 1L;
		nonexistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L; //encontramos 21 vezes PCGamer para ser certo
		
		pageRequest = PageRequest.of(0, 10);
	}
	
	@Test
	public void findShouldReturnAllProductsWhenNameIsEmpty() {
		String name = "";
		
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoringCase() {
		String name = "pc gAMeR";
		
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExists() {
		String name = "PC Gamer";
		
		
		Page<Product> result = repository.find(null, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Optional<Product> result = repository.findById(product.getId());
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1L, product.getId()); //comprar com novo id que devera gerar
		Assertions.assertTrue(result.isPresent()); //se o novo id esta presente depois de o ter salvo
		
		Assertions.assertSame(result.get(), product); //acessar objeto product dentro do optional fazemos com o get //usamos este para ver se objetos tem a mesma referencia ou seja se sao o mesmo, ou seja vemos assim se sao o mesmo objeto o que criamos e o que fomos buscar
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		//retorna sempre um optional mas depois o optinal tem o metodo ispresent para sabermos se de facto tem algum objeto
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonexistingId);
		});
		
	}
}

