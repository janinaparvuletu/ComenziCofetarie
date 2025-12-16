package com.example.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Rol;
import com.example.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer>{
	
	boolean existsByCnp(String cnp);
	Optional<User> findByCnp(String cnp);
	//void deleteByCnp(String cnp);
	void deleteByCnp(String cnp);
	List <User> findByUsername(String name);
	List<User> findByRol(Rol rol);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);

}
