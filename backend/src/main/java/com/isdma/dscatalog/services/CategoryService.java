package com.isdma.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.dto.CategoryDTO;
import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.repositories.CategoryRepository;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;


@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)//garanto que metodo executa transacção com banco de dados e abre e fecha transação quando deve e alem disso nao travo o bando de dados so para ler dados com o readonly=true
	public List<CategoryDTO> findAll(){
		
		List<Category> list = repository.findAll();
		
		/*List<CategoryDTO> listdto = new ArrayList<>();
		
		for(Category cat : list) {
			listdto.add(new CategoryDTO(cat));
		}*/
		
		//A MESMA COISA MAS USANDO EXPRESSÂO LAMBDA
		//com o stream chamamos um super metodo, metodos que podem receber metodos, e la colocamos uma expressao lambda com que que queremos fazer, converter cada Category para CategoryDTO depois voltamos a converter de stream para list
		//List<CategoryDTO> listdto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		
		Optional<Category> obj= repository.findById(id);
		
		Category cat = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));//Em vez de colocar apenas get para buscar a category, eu pego esta função que me lança excepção caso seja nulo o objeto, assim crio uma excepção na minha entidade que as trata atraves de função lambda que neste caso nao retorna nada ()

		
		return new CategoryDTO(cat);
	}
	

}
