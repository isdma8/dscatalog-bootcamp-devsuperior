package com.isdma.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //para dizer que iremos usar o UTC para o tempo, ou se nao especificarmos o timezone, -3 etc entao será UTC
	private Instant createdAt; //para efeitos de Log
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;
	
	@ManyToMany(mappedBy = "categories") //como ja definimos a relação em categories, aqui basta colocar o nome dado ao set lá que no caso foi categories
	private Set<Product> products = new HashSet<>();
	
	public Category() {
		
	}

	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	/*public void setCreatedAt(Instant createdAt) { //NAO FAZ SENTIDO TER NESTES CASOS
		this.createdAt = createdAt;
	}*/
	
	public Set<Product> getProducts() {
		return products;
	}

	/*
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}*/
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	//Metodo auxiliar para ele saber onde gravar, porque sempre que criar uma categoria sera CreatedAt e se for atualizar sera updatedAt
	@PrePersist
	public void prePersist() {//esse nome porque é feito antes de precistir, antes de salvar
		createdAt = Instant.now();
	}
	
	@PreUpdate   //com isto o JPA faz isto antes de gravar caso seja nova a de cima, caso seja update a de baixo antes de gravar e fica automatizado
	public void preUpdate() {
		updatedAt = Instant.now();
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
