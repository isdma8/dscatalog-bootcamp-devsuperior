package com.isdma.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isdma.dscatalog.dto.CategoryDTO;
import com.isdma.dscatalog.services.CategoryService;

//O nosso recourse implementa o controller Rest, é a nossa API(Application programming interface) do nosso backend
//sao os recursos disponibilizados para os  frontoffice
//Neste caso esta classe é o recurso da entidade Category

@RestController //Annotation para o Spring saber que é uma class de resources, forma de implementar algo que ja foi implementado
@RequestMapping(value = "/categories") //rota REST do nosso recurso
public class CategoryResource {

	@Autowired   //Para injetar automaticamente as dependencias
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		
		List<CategoryDTO> list = service.findAll();
		
		/*List<Category> list = new ArrayList<>();
		list.add(new Category(1L,"Books"));  
		list.add(new Category(2L, "Electronics"));*/
		
		return ResponseEntity.ok(list);
		
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		
		CategoryDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
		
	}
	
}
