package com.isdma.dscatalog.tests.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isdma.dscatalog.dto.ProductDTO;
import com.isdma.dscatalog.services.ProductService;
import com.isdma.dscatalog.services.exceptions.DatabaseException;
import com.isdma.dscatalog.services.exceptions.ResourceNotFoundException;
import com.isdma.dscatalog.tests.factory.ProductFactory;

@SpringBootTest
@AutoConfigureMockMvc //para nao carregar seridor tomcat
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean //carrego o contexto mas so quero trocar um bean pelo mockado neste caso o ProductService, assim garanto que sera um teste de unidade do meu ProductResource e apenas dele
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper; //para podermos converter o nosso objeto java em objeto json para enviar dados do create e update
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private ProductDTO newProductDTO;
	private ProductDTO existingProductDTO;
		
	private PageImpl<ProductDTO> page;
	
	private String operatorUsername;
	private String operatorPassword;
	
	@BeforeEach
	void setUp() throws Exception{
		
		operatorUsername = "alex@gmail.com";
		operatorPassword = "123456";
		
		existingId = 1L;
		nonExistingId =2L;		//nao importa os numeros que colocamos porque é um teste de unidade, nos é que estabelecemos as regras, para os o existing é 1 e o nao existente é 2
								//e com base nisso fazemos a simução do nosso mock
		dependentId = 3L;
		newProductDTO = ProductFactory.createProductDTO(null);
		existingProductDTO = ProductFactory.createProductDTO(existingId);
		
		page = new PageImpl<>(List.of(existingProductDTO)); //basta meter 1 produto na lista, é so para teste
		
		//Configuração do mockbean do service
		when(service.findById(existingId)).thenReturn(existingProductDTO);
		
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		//configurar service mockado para retornar uma pagina tal como precisamos para o test do findAll
		when(service.findAllPaged(any(), anyString(), any())).thenReturn(page);
		
		when(service.insert(any())).thenReturn(existingProductDTO); //tem de returnar um objeto criado quando crio um qualquer
		
		when(service.update(eq(existingId), any())).thenReturn(existingProductDTO); //a partir do momento que usamos o any temos de meter todos os argumentos com a mesma classe entao o eq pertence a mesma classe e é a maneira de passar um id por la
	
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		//quando nao retorna nada começamos sem ser com o when
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
		
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		
		String expectedName = newProductDTO.getName();
		Double expectedPrice = newProductDTO.getPrice();
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)); 
		
				result.andExpect(status().isOk());
				
				result.andExpect(jsonPath("$.id").exists());
				result.andExpect(jsonPath("$.id").value(existingId)); //testar se id da resposta é igual
				result.andExpect(jsonPath("$.name").value(expectedName));
				result.andExpect(jsonPath("$.price").value(expectedPrice));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/products/{id}", nonExistingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)); 
		
				result.andExpect(status().isNotFound()); 
	}
	
	
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		
		ResultActions result = 
				mockMvc.perform(get("/products")
				.accept(org.springframework.http.MediaType.APPLICATION_JSON)); 
		
				result.andExpect(status().isOk()); 
		
				result.andExpect(jsonPath("$.content").exists());//verificar se este objeto existe la no on«bjeto json, ou sej verificar que o content existe na resposta
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception{
		
		ResultActions result = 
				mockMvc.perform(get("/products/{id}", existingId)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON)); //depois de fechar aqui o perform é que faço as assertions abaixo
		
				result.andExpect(status().isOk()); //estou a fazer uma asserion que vai ter de retornar o status 200
				
				result.andExpect(jsonPath("$.id").exists()); //ver se existe o id
				
				result.andExpect(jsonPath("$.id").value(existingId)); //ver se é o mesmo inicial
		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON)); 
		
				result.andExpect(status().isNotFound());//aqui eu espero o 404 sabendo que o mocado em cima ja passou a excepção temos de ver como se comporta aqui o resource
	}
	
	
	//função para ir buscar o token, tal como se tivessemos a pedi lo no postman mas aqui temos de converter string ate conseguir token apenas
	//dado o user e senha ele faz reuição na api sendo retornado um string com o access-token da app, como é uma api autenticada precisamos disto para fazer os testes
	private String obtainAccessToken(String username, String password) throws Exception {
		 
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("client_id", clientId);
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result 
	    	= mockMvc.perform(post("/oauth/token")
	    		.params(params)
	    		.with(httpBasic(clientId, clientSecret))
	    		.accept("application/json;charset=UTF-8"))
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType("application/json;charset=UTF-8"));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}	
}