package com.isdma.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {//tera tudo o que o StandardError + o que quisermos
	private static final long serialVersionUID = 1L;

	
	private List<FieldMessage> errors = new ArrayList<>();
	
	//Contrutores ja os herda
	
	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message)); //adiciono à lista de erros cada objeto erro como definimos
	}

}
