package com.isdma.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.isdma.dscatalog.components.JwtTokenEnhancer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	@Value("${jwt.duration}")
	private Integer jwtDuration;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenEnhancer tokenEnhancer; //para adicionarmos coisas ao token injetamos a classe criada
	
	
	//Aqui temos os 4 beans que precisamos e que criamos na classe AppConfig e websecurityconfig
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient(clientId) //frontend tem de informar destes dados da aplicação no token para ter acesso
		.secret(passwordEncoder.encode(clientSecret))    //mais tarde iremos colocar em arquivo	
		.scopes("read", "write") //tipo de acesso
		.authorizedGrantTypes("password") //ha varios tipos no auth, temos de usar o que pretendemos, estao descritos no auth os existentes
		.accessTokenValiditySeconds(jwtDuration); //1 dia em segundos em que o tokin é valido 86400
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer)); //espera uma lista para podermos passar o token passamos tmabem o accesstokenconveter
		
		//Aqui colocamos quem vai autorizar e qual o formato do tokin
		endpoints.authenticationManager(authenticationManager) //vai ser o bean que criei
		.tokenStore(tokenStore) //vai ser o bean que injetamos tambem
		.accessTokenConverter(accessTokenConverter)//outro que definimos 
		.tokenEnhancer(chain); //assim passamos as nossas adições ao header do token
	}

	
}
