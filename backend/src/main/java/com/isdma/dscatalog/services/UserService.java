package com.isdma.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger; //Logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dscatalog.dto.RoleDTO;
import com.isdma.dscatalog.dto.UserDTO;
import com.isdma.dscatalog.dto.UserInsertDTO;
import com.isdma.dscatalog.dto.UserUpdateDTO;
import com.isdma.dscatalog.entities.Role;
import com.isdma.dscatalog.entities.User;
import com.isdma.dscatalog.repositories.RoleRepository;
import com.isdma.dscatalog.repositories.UserRepository;
import com.isdma.dscatalog.services.exceptions.DatabaseException;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;


@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);//colocar a class onde ta rodando
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pagerequest) {
		Page<User> list = repository.findAll(pagerequest);
		
		//no caso o page já é um stream entao ja nem precisamos chamar o stream e converter de novo no fim como no findall
		return list.map(x -> new UserDTO(x));
		
	}
	

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {

		Optional<User> obj = repository.findById(id);

		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));// Em vez de colocar
																									// apenas get para
																									// buscar a
																									// category, eu pego
																									// esta função que
																									// me lança excepção
																									// caso seja nulo o
																									// objeto, assim
																									// crio uma excepção
																									// na minha entidade
																									// que as trata
																									// atraves de função
																									// lambda que neste
																									// caso nao retorna
																									// nada ()

		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		//cat.setName(dto.getName());
		
		//Como é igual no insert e no update vamos fazer um metodo auxiliar que faz o mesmo transformar o UserDTO no product
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));//primeiro criptografamos a pass e depois gravamos na bd

		entity = repository.save(entity); // retorna o cat inserido, neste caso muda que fica ja com id

		return new UserDTO(entity);
	}


	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id); // a diferença para o findbyid é que ele nao vai no banco de dados,
														// ele instancia um objeto provisorio com esse id
			//entity.setName(dto.getName()); // atualizei os dados da entidade que está so na memoria
			
			copyDtoToEntity(dto, entity);
			
			entity = repository.save(entity); // agora sim acedemos para salvar, pode dar excepção porque pode nao
												// existir o id entao fazemos um try catch

			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);// Caso nao encontre lançamos a nossa excepção
																		// personalizada
		}
	}

	
	public void delete(Long id) {//excepcionalmente no delete nao usamos a anotation @Transactional porque senao nao conseguimos capturar as exceções que a bd nos manda
		try {

			repository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);// Caso nao encontre lançamos a nossa excepção
																		// personalizada
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation"); //caso a categoria tenha produtos, não a posso apagar entao a bd lança exceptção
		}
	}

	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear(); //limpar porque porventura pode ter alguma ainda
		for(RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	//implementação do metodo obrigatorio implemtar do UserDetailService do Spring Security
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("User not found: "+ username);
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
	
}
