package com.isdma.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig { //Uma classe que usamos apra alguma configuração, algum componente qu precisemos

	@Bean //é um anotation nao de classe mas de metodo
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //retorna me uma insancia nova do password enconder, e esta intancia sera um componete gerenciado pelo Spring boot isto porque colocamos o anotation Bean, assim posso injeta lo em outras classes, em outros componentes
	}
	
	
	//objetos capazes de acessar objeto Token JWT
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("MY-JWT-SECRET");
		return tokenConverter;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
