package com.uuhnaut69.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket buildDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).select()
				.apis(RequestHandlerSelectors.basePackage("com.uuhnaut69.todo.controller")).paths(PathSelectors.any())
				.build();
	}

	private ApiInfo buildApiInf() {
		return new ApiInfoBuilder().title("RESTful API").description("Rest API for Todo App").version("1.0").build();
	}
}