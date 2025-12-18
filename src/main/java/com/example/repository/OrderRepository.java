package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;
import com.example.entity.Status;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	List <Order> findByUserUsernameAndUserNrTelefon(String username, String nrTelefon);
	List <Order> findByStatus(Status status);
	List<Order> findByUserEmail(String email);
}
