package com.isdma.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)//garanto que metodo executa transacção com banco de dados e abre e fecha transação quando deve e alem disso nao travo o bando de dados so para ler dados com o readonly=true
	public List<Category> findAll(){
		return repository.findAll();
	}

}
