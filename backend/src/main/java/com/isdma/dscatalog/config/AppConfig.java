package com.isdma.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig { //Uma classe que usamos apra alguma configuração, algum componente qu precisemos

	@Bean //é um anotation nao de classe mas de metodo
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //retorna me uma insancia nova do password enconder, e esta intancia sera um componete gerenciado pelo Spring boot isto porque colocamos o anotation Bean, assim posso injeta lo em outras classes, em outros componentes
	}
}
