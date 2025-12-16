package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name="users")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="user_name")
	private String username;
	@Column(unique = true)
	private String email;
	private String password;
	@Column(unique = true)
	private String cnp;
	@Column(name="data_nasterii")
	private LocalDate dataNasterii;
	@Column(name="nr_telefon")
	private String nrTelefon;
	@Enumerated(EnumType.STRING)
	private Rol rol;
	
	public void setPassword(String password)
	{
		this.password=password;
	}
}
