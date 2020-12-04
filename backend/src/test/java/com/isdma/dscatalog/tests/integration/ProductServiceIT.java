package com.isdma.dscatalog.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.dto.ProductDTO;
import com.isdma.dscatalog.entities.Product;
import com.isdma.dscatalog.services.ProductService;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest  //agora preciso carregar o contexto as injecoes de dependencia entao uso este poruqe agora irei fazer testes com repositorys reais
@Transactional //para cada teste deixar o bd tal como estava, cada teste tem de ser independente dos outros senao tinhamos de andar sempre a mudar os dados para ter isso em conta porque se um testes apaga-se um id o seguinte já ia fazer o teste com menos um id na bd
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	
	private Long existingId;
	private Long nonExistingId;
	
	private Long countTotalProducts;
	private Long countPCGamerProducts;
	private PageRequest pageRequest;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L; //aqui ja é importante ter logica este id, ou seja ele deve nao existir no db porque aqui estamos a verificar dados reais
		
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		pageRequest = PageRequest.of(0, 10);
	}
	
	/*@Test  //Neste caso este test nao faz sentido porque num caso real como este com o repository real nao ha dependecia obrigatoria entre tabelas no modelo de dominio atualmente entao nao faz sentido para já
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() { //este test nem era preciso porque neste momento nenhuma classe depende de produto mas imaginemos que no futuro queremos evoluir o nosso modelo de dominio e ter por exemplo item de produto, entao ja ficaria aqui o test para validar isso
		
		
		Assertions.assertThrows(DatabaseException.class, () -> { 
			service.delete(dependentId);
		});
		
		
	}*/
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> { //ResourceNotFoundException é a excepção do service é esta que teriamos de verificar aqui
			service.delete(nonExistingId);
		});
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		
		Assertions.assertDoesNotThrow(() -> { //se nao houver excepcoes ta tudo bem, parecido com assertrow
			service.delete(existingId);
		});
		
	}
	
	//Testes agora sao a partir do Service
	
	@Test
	public void findAllPagedShouldReturnNothingWhenNameDoesNotExist() {
		String name = "Camera";
		
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnAllProductsWhenNameIsEmpty() {
		String name = "";
		
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnProductsWhenNameExistsIgnoringCase() {
		String name = "pc gAMeR";
		
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}

	
}