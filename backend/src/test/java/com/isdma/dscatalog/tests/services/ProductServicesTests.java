package com.isdma.dscatalog.tests.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.isdma.dscatalog.dto.ProductDTO;
import com.isdma.dscatalog.entities.Product;
import com.isdma.dscatalog.repositories.ProductRepository;
import com.isdma.dscatalog.services.ProductService;
import com.isdma.dscatalog.services.exceptions.DatabaseException;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;
import com.isdma.dscatalog.tests.factory.ProductFactory;

@ExtendWith(SpringExtension.class)   //este é o mais simpes, nao carrega o contexto, os beans do sistema, apenas o basico do service entao usamos o Mock basico e nao o MockBean do Spring
public class ProductServicesTests {

	@InjectMocks //objetos que trabalhamos diretamente
	private ProductService service;
	
	//temos que mocar uma dependencia para o repository porque na verdade so podemos usar de verdade o service depois o resto que precisarmos temos de criar aqui algo que simule o comportamento do repository por exemplo
	
	@Mock //objetos dos quais ira depender o service
	private ProductRepository repository;  //quando criamos mock nao podemos esqucer de configurar o comportamento simulado dele
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Product product;
	private PageImpl<Product> page; //nao é o page Interface, é o pagimpque é uma implementação de Page, é um objeto concreto
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L; //supor que o 4L tem um outra classe que depende dele e por isso nao pode ser apagado
		product = ProductFactory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		//Aqui configuro os comportamentos simulados para os meus objetos mockados //criamos o comportamento simulado deles
		
		Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any())) //ArgumentMatchers para definir objetos nao concretos porque nao temos de passar alguma coisa, so temos de testar e que nos interessa no caso é se retorna uma pagina depois de fazer este pedido
			.thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(Optional.of(product)); // para qulquer objeto produto que eu salve ele tera de retornar product
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product)); //Optional.of com isto podemos criar uma instancia do optional //usamos o when quando sabemos que vamos ter retorno da chamada e depois especificamos o resultado, o meu repository mocado vai ter de retornar um optional com um produto la dentro neste caso
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());//intanciar optional vazio que é o que deve retornar quando nao existe o id
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getOne(nonExistingId);
		
		Mockito.doNothing().when(repository).deleteById(existingId); //donothing é para os casos que sabemos que nao retorna nada
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() { //este test nem era preciso porque neste momento nenhuma classe depende de produto mas imaginemos que no futuro queremos evoluir o nosso modelo de dominio e ter por exemplo item de produto, entao ja ficaria aqui o test para validar isso
		ProductDTO dto = new ProductDTO(); //podia usar fabrica
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> { 
			service.update(nonExistingId, dto);
		});
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = new ProductDTO();
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
	}
	
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() { //este test nem era preciso porque neste momento nenhuma classe depende de produto mas imaginemos que no futuro queremos evoluir o nosso modelo de dominio e ter por exemplo item de produto, entao ja ficaria aqui o test para validar isso
		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> { 
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Long categoryId = 0L;
		String name = "";
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(categoryId, name, pageRequest);
		
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Mockito.verify(repository, Mockito.times(1)).find(null, name, pageRequest);
		
	}
	
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() { //este test nem era preciso porque neste momento nenhuma classe depende de produto mas imaginemos que no futuro queremos evoluir o nosso modelo de dominio e ter por exemplo item de produto, entao ja ficaria aqui o test para validar isso
		
		
		Assertions.assertThrows(DatabaseException.class, () -> { 
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);  //saber se foi chamado
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> { //ResourceNotFoundException é a excepção do service é esta que teriamos de verificar aqui
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId); //assim vai verificar se o delebyid foi chamado na ação feita acima no teste, Mockito.times(2) quantas vezes espero que o metodo deletebyid seja chamado neste caso por exemplo
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		
		Assertions.assertDoesNotThrow(() -> { //se nao houver excepcoes ta tudo bem, parecido com assertrow
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId); //assim vai verificar se o delebyid foi chamado na ação feita acima no teste, Mockito.times(2) quantas vezes espero que o metodo deletebyid seja chamado neste caso por exemplo
	}
	
	
}
