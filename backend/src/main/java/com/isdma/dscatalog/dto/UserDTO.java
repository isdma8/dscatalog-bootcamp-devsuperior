package com.isdma.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.isdma.dscatalog.entities.User;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank(message= "Campo obrigatório") //Melhor o noblank que o notempty poruqe este ve mesmo que o user tenha metido espaços em branco no campo a inserir
	private String firstName;
	private String lastName;
	
	@Email(message = "Por favor inserir um email válido")
	private String email;
	//private String password; Nao queremos a passaword a transitar
	
	public UserDTO() {
		 
	}
	
	Set<RoleDTO> roles = new HashSet<>();

	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}



	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		//this.password = entity.getPassword();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role))); //podemos fazer isto porque carregamos a lista de roles juntamente com o user com a ajuda do paramentro da anotation fetch
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
	
	
	
	
	
	
}
