package com.isdma.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
		return ResponseEntity.ok().body(dto);
		
	}
	
	@PostMapping    //Post para inserir
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto); 
		//por defeito com o ok dá o codigo de suecesso 200 de requesição com sucesso mas nos queremos neste caso o 201 que quer dizer inserção com sucesso
		//Neste caso até vamos querer alem do codigo 201 ter no cabeçalho o caminho para o novo objeto criado
	}
	
}
