package com.isdma.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.dto.CategoryDTO;
import com.isdma.dscatalog.dto.ProductDTO;
import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.entities.Product;
import com.isdma.dscatalog.repositories.CategoryRepository;
import com.isdma.dscatalog.repositories.ProductRepository;
import com.isdma.dscatalog.services.exceptions.DatabaseException;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	//@Transactional(readOnly = true) // garanto que metodo executa transacção com banco de dados e abre e fecha
									// transação quando deve e alem disso nao travo o bando de dados so para ler
									// dados com o readonly=true
	//public List<ProductDTO> findAll() {

		//List<Product> list = repository.findAll();

		/*
		 * List<ProductDTO> listdto = new ArrayList<>();
		 * 
		 * for(Product cat : list) { listdto.add(new ProductDTO(cat)); }
		 */

		// A MESMA COISA MAS USANDO EXPRESSÂO LAMBDA
		// com o stream chamamos um super metodo, metodos que podem receber metodos, e
		// la colocamos uma expressao lambda com que que queremos fazer, converter cada
		// Product para ProductDTO depois voltamos a converter de stream para list
		// List<ProductDTO> listdto = list.stream().map(x -> new
		// ProductDTO(x)).collect(Collectors.toList());

		//return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());

	//}
	
	@Transactional(readOnly = true) 
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, PageRequest pagerequest) {
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId)); //podia passar id mas aqui convem sempre instanciar o objeto primeiro
		Page<Product> list = repository.find(categories, name, pagerequest);
		
		//no caso o page já é um stream entao ja nem precisamos chamar o stream e converter de novo no fim como no findall
		return list.map(x -> new ProductDTO(x));
		
	}
	

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Optional<Product> obj = repository.findById(id);

		Product pro = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));// Em vez de colocar
																									// apenas get para
																									// buscar a
																									// category, eu pego
																									// esta função que
																									// me lança excepção
																									// caso seja nulo o
																									// objeto, assim
																									// crio uma excepção
																									// na minha entidade
																									// que as trata
																									// atraves de função
																									// lambda que neste
																									// caso nao retorna
																									// nada ()

		return new ProductDTO(pro, pro.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		//cat.setName(dto.getName());
		
		//Como é igual no insert e no update vamos fazer um metodo auxiliar que faz o mesmo transformar o ProductDTO no product
		copyDtoToEntity(dto, entity);
		
		/*if(entity.getCategories().size() == 0) { //adicionar uma cat para ter pelo menos uma senao da erro enquanto nao é corrigido no frontend
			Category cat = categoryRepository.getOne(1L);
			entity.getCategories().add(cat);
		}*/

		entity = repository.save(entity); // retorna o cat inserido, neste caso muda que fica ja com id

		return new ProductDTO(entity);
	}
	


	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id); // a diferença para o findbyid é que ele nao vai no banco de dados,
														// ele instancia um objeto provisorio com esse id
			//entity.setName(dto.getName()); // atualizei os dados da entidade que está so na memoria
			
			copyDtoToEntity(dto, entity);
			
			entity = repository.save(entity); // agora sim acedemos para salvar, pode dar excepção porque pode nao
												// existir o id entao fazemos um try catch

			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);// Caso nao encontre lançamos a nossa excepção
																		// personalizada
		}
	}

	
	public void delete(Long id) {//excepcionalmente no delete nao usamos a anotation @Transactional porque senao nao conseguimos capturar as exceções que a bd nos manda
		try {

			repository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);// Caso nao encontre lançamos a nossa excepção
																		// personalizada
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation"); //caso a categoria tenha produtos, não a posso apagar entao a bd lança exceptção
		}
	}

	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());

		entity.getCategories().clear(); //limpar porque porventura pode ter alguma ainda
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
	
}
