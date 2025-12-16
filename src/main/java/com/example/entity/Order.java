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
	private int id;
	
	@ManyToOne
	@JoinColumn
	private User user;
	
	@Enumerated(EnumType.STRING)
	private Blat blat;
	
	@Enumerated(EnumType.STRING)
	private Crema crema;
	
	@Enumerated(EnumType.STRING)
	private  Glazura glazura;
	
	@Column(name="mesaj_tort")
	private String mesajTort;
	
	private LocalDate deadline;
	
	
}
