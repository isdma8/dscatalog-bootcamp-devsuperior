package com.isdma.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	
}
