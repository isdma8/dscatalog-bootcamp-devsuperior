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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TEXT")//para podermos inserir muito texto neste campo senao estoira	
	private String description;
	private Double price;
	private String imgUrl;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //Colocar para o frontoffice quando vir a data saber que ta definido como UTC, vai instruir BD para que armazene a data no UTC
	private Instant date; // este campo nao esta no diagrama acrescentamos para ficar a saber como se faz

	
	//Assim que faz a definição de uma associação de muitos para muitos, estas normalmente têm uma tabela com as chaves estrangeiras de uma tabela e outra para associar uma à outra, basicamente definimos aqui a tabela
	@ManyToMany
	@JoinTable(name="tb_product_category",
		joinColumns = @JoinColumn(name = "product_id"), //numa associação muitos para muitos colocamos em primeiro o id da tabela onde estamos neste caso product
		inverseJoinColumns = @JoinColumn(name = "category_id"))
	Set<Category> categories = new HashSet<>();// colocamos Set em vez de List porque o Set nao admite repetições e nós
												// não queremos que um produto tenha 2x a mesma categoria, pode ter
												// varias mas não repetidas
	// Set tal como List é uma interface então não da para inicializar com List mas
	// sim com a implementação dela Hashset
	// Listas temos de inicializar logo

	public Product() {

	}

	public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		//No contrutor não informamos coleções
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Set<Category> getCategories() {//Coleções so tem Set, não faz sentido mudar coleções
		return categories;
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
