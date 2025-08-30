package com.season.livingmate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwagerConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		String jwtSchemeName = "JWT";

		// Security Requirement 설정
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

		// Security Scheme 설정 (헤더에 Authorization: Bearer {token})
		Components components = new Components()
				.addSecuritySchemes(jwtSchemeName,
						new SecurityScheme()
								.name(jwtSchemeName)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT"));

		return new OpenAPI()
			.info(new Info()
				.title("Livingmate API")
				.description("Livingmate REST API 문서입니다.")
			)
				.addSecurityItem(securityRequirement)
				.components(components);
	}
}
