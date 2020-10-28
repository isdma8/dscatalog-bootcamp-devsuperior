package com.isdma.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.isdma.dscatalog.dto.UserUpdateDTO;
import com.isdma.dscatalog.entities.User;
import com.isdma.dscatalog.repositories.UserRepository;
import com.isdma.dscatalog.resources.exceptions.FieldMessage;

//codigo padrao para criar uma anotation personalizada nossa, so temos de especificar aqui a classe a qual queremos 
//validar os campos os quais precisamos de validar especificamente com acesso a base de dados
//neste caso colocamos a classe UserInsertDTO
//fazendo desta forma podemos aproveitar todas as potencialidades de uma anotation normal de validação
//assim como a maneira de passar excepções ja criada

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	@Autowired
	private HttpServletRequest request; //para podermos aceder ao id que ta na url do pedido, este servlet guarda as informaçoes da requisição
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) { //Aqui é a parte que alteramos
		
		@SuppressWarnings("unchecked") //apaga o warning que nos deu por termos convertido assim abaixo para map chave valor de strin string, "id" "2"
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); //é um dicionario dos atriburos que fica na minha urivars
		long userId = Long.parseLong(uriVars.get("id")); //converto o que chega em string para long
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		//Aqui colocamos as validações que precisarmos, ifs basicamente que adicionam ou nao erros à lista, depois no fim esta classe testa se esta vazia ou nao e manda true ou false de retorno
		//aproveitamos assim o ciclo de vida do beansvalidation
		
		User user = repository.findByEmail(dto.getEmail());
		if(user != null && userId != user.getId()) { //o email nao pode ser igual a nenhum outro que nao o dele proprio para poder altera lo depois
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