package com.isdma.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.isdma.dscatalog.dto.UserInsertDTO;
import com.isdma.dscatalog.entities.User;
import com.isdma.dscatalog.repositories.UserRepository;
import com.isdma.dscatalog.resources.exceptions.FieldMessage;

//codigo padrao para criar uma anotation personalizada nossa, so temos de especificar aqui a classe a qual queremos 
//validar os campos os quais precisamos de validar especificamente com acesso a base de dados
//neste caso colocamos a classe UserInsertDTO
//fazendo desta forma podemos aproveitar todas as potencialidades de uma anotation normal de validação
//assim como a maneira de passar excepções ja criada

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) { //Aqui é a parte que alteramos
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		//Aqui colocamos as validações que precisarmos, ifs basicamente que adicionam ou nao erros à lista, depois no fim esta classe testa se esta vazia ou nao e manda true ou false de retorno
		//aproveitamos assim o ciclo de vida do beansvalidation
		
		User user = repository.findByEmail(dto.getEmail());
		if(user != null) {
			list.add(new FieldMessage("email", "Email que tenta inserir já existe"));
		}
		
		
		for (FieldMessage e : list) { //serve par inserir os nossos erros nas mensagens do proprio beansvalidation para depois aparecerem la quando pesquisarmos nas excepçoes os erros
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}