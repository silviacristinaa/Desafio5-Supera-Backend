package br.com.banco.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket getBean() { 
		return new Docket(DocumentationType.SWAGGER_2).select()
			.paths(PathSelectors.regex("/api/transfers.*"))
			.build()
			.apiInfo(getInfo());
	}
	
	private ApiInfo getInfo() { 
		return new ApiInfoBuilder()
			.title("Transferência")
			.description("Serviço para o fornecimento de dados de transferência")
			.contact(new Contact("Silvia Cristina Alexandre", "https://www.linkedin.com/in/silvia-cristina-alexandre", 
						         "silviacristinaalexandre1@gmail.com"))
			.build();
	}
}