package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name="orders")
public class Order {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private LocalDate deadline; 
		
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Enumerated(EnumType.STRING)
	private Blat blat;
	
	@Enumerated(EnumType.STRING)
	private Crema crema;
	
	@Enumerated(EnumType.STRING)
	private  Glazura glazura;
	
	@Column(name="mesaj_tort")
	private String mesajTort;
	
	public void setStatus(Status status)
	{
		this.status=status;
	}
	
	public void setUser(User user)
	{
		this.user=user;
	}	
	
	
}
