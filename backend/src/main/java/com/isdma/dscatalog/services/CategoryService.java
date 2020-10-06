package com.isdma.dscatalog.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.dto.CategoryDTO;
import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.repositories.CategoryRepository;
import com.sun.el.stream.Stream;

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
	

}
