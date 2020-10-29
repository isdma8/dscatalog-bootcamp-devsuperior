package com.isdma.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableResourceServer    //para que esta classe implemente a funcionalidade de ResourceServerdoOuauth2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private Environment env;
	
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };//Liberado para todos estes links
	
	private static final String[] OPERATOR_OR_ADMIN= { "/products/**", "/categories/**" }; //todos os pedidos acima do link base são rotas sem ser get entao ficam disponiveis para estes, ** é tudo
	
	private static final String[] ADMIN = { "/users/**" };
	
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		//aqui configuramos o tokenstore	//aqui assim vai poder analisar se o token ta batendo certo com o secret o tempo de expiração etc
		resources.tokenStore(tokenStore); //recebendo o nosso bean tokenstore
		
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//Liberar H2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) { //se estou a rodar num profile teste eu quero liberar o acesso, liberar o h2
			http.headers().frameOptions().disable(); //o h2 requer que eu desabilite isto 
		}
		
		//Configurar as Rotas
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll() //Não se exige login a estes
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll() //libetar apenas o metodo get nestes caras
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")//as rotas deste role pode acessar quem tiver algum destes papeis, no bd ta ROLE_PAPEL, aqui so basta o PAPEL
		.antMatchers(ADMIN).hasAnyRole("ADMIN") //so pode andar em admi quem tiver logado como admin
		.anyRequest().authenticated(); //para qualquer outra rota tem de tar logado nao importando o perfil de user
	}

	
	
}
