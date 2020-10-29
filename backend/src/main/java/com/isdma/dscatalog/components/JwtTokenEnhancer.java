package com.isdma.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.isdma.dscatalog.entities.User;
import com.isdma.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {//para adicionarmos coisas ao header do token, esta class podia tar em services mas criamos o components mais ou menos para o mesmo mas nao nas classes entity


		User user = userRepository.findByEmail(authentication.getName()); //authentication.getName() o username que ja anda pelo token
		
		Map<String, Object> map = new HashMap<>(); //preciso de um map par chave valor, chave string valor neste caso tem se ser object porque pode ser qualquer coisa
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		//tipo igual ao que chega mas um pouco mais especifico porque tem logo o metodo para inserir
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)accessToken; //downcasting para converter para o tipo que eu quero
		token.setAdditionalInformation(map);
		
		return accessToken; //adicionei nele agora retorno o, mas posso retornar o token mesmo é igual, o DefaultOAuth2AccessToken é uma subclass do OAuth2AccessToken que tem o metodo set implemtado entao so com ele podemos adicionar elementos por isso fizemos o downcasting para uma tipo mais especifico do que o objeto que chega
	} 

}
