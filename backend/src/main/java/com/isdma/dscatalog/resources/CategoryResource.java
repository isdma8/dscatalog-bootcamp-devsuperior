package com.isdma.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isdma.dscatalog.entities.Category;

//O nosso recourse implementa o controller Rest, é a nossa API(Application programming interface) do nosso backend
//sao os recursos disponibilizados para os  frontoffice
//Neste caso esta classe é o recurso da entidade Category

@RestController //Annotation para o Spring saber que é uma class de resources, forma de implementar algo que ja foi implementado
@RequestMapping(value = "/categories") //rota REST do nosso recurso
public class CategoryResource {

	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		List<Category> list = new ArrayList<>();
		list.add(new Category(1L,"Books"));  
		list.add(new Category(2L, "Electronics"));
		
		return ResponseEntity.ok(list);
		
	}
	
}
