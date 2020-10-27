package com.isdma.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	//definimos este metodo aqui porque se nao definirmos nao aparece nas pesquisas de metodos como findall findbyid, mais genericos esses estao logo implementados mas estes mais especificos de pesquisa pelos campos da entidade temos de definir explicitamente para aparecerem
	//Pesquisar user por email, precisamos para o UserInsertValidator
	User findByEmail(String email);
}
