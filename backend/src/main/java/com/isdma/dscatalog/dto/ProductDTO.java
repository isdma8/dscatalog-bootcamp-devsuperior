package com.isdma.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.isdma.dscatalog.entities.Category;
import com.isdma.dscatalog.entities.Product;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	//Anotamos nos DTOs porque sao eles que passam nas requisições, alem de colocar aqui tenh
	//de ir ao resource e la nas requisições colocamos @valid no pedido de inserção
	
	private Long id;

	@Size(min=5, max = 60, message = "Campo dererá ter entre 5 e 60 caracteres")
	@NotBlank(message = "Campo obrigatório")
	private String name;
	
	@NotBlank(message = "Campo obrigatório")
	private String description;
	
	@Positive(message = "Deverá inserir um valor positivo")
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "A data o produto não pode ser futura")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>(); //aqui vamos meter esta porque no front tem a opção de escolher logo a categoria que queremos para o produto
	
	public ProductDTO() {
		
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date; 
	}
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}

	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity); //recebo um contrutor que recebe so a entidade entao executa logo tudo com o construtor acima
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat))); //podiamos fazer um for normal mas podemos fazer com esta função de alta ordem e com lambda la dentro faço tudo
								//percorro o conjunto de categorias que chegam e para cada cat que chega eu a adiciono ao conjunto de categorias do ProductDTO transformando-as para isso em CategoryDTO cada uma delas
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

	public List<CategoryDTO> getCategories() {
		return categories;
	}

}
