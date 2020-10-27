package com.isdma.dscatalog.dto;

import com.isdma.dscatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {//herança de UserDTO, ou seja vai ter tudo o que tem o UserDTO
	private static final long serialVersionUID = 1L; 

	private String password;
	
	public UserInsertDTO() {
		super(); //para garantir que caso existe alguma implemntação no construtor vazio da superclass ele pega tambem e executa ela
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
