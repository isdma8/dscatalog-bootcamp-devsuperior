package com.isdma.dscatalog.dto;

import com.isdma.dscatalog.services.validation.UserInsertValid;
import com.isdma.dscatalog.services.validation.UserUpdateValid;

@UserUpdateValid //temos de criar esta classe para colocar aqui esta anotation das validaçoes personalizada
				//isto porque se a colocassemos no UserDTO, o UserInsertDTO ia herdar esta anotation, sendo que ele tem já uma anotation especifica dele
public class UserUpdateDTO extends UserDTO {
	private static final long serialVersionUID = 1L; 

	
}
