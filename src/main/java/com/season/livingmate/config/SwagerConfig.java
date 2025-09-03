package com.season.livingmate.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;

@Configuration
public class SwagerConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		String jwtSchemeName = "JWT";
		String headerName = "Authorization"; // 실제 요청 헤더 이름

		// Security Requirement 설정
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

		// Security Scheme 설정 (헤더에 Authorization: Bearer {token})
		Components components = new Components()
				.addSecuritySchemes(jwtSchemeName,
						new SecurityScheme()
								.name(headerName)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT"));

		OpenAPI openAPI = new OpenAPI()
				.info(new Info()
						.title("Livingmate API")
						.description("Livingmate REST API 문서입니다.")
				)
				.components(components)
				.addSecurityItem(securityRequirement)
				.servers(List.of(new Server().url("https://livingmate.store")));

		PathItem logoutPath = new PathItem().post(new Operation()
				.operationId("logout")
				.summary("로그아웃 (Spring Security CustomLogoutFilter 처리)")
				.description("JWT 무효화/삭제 등 로그아웃 로직은 CustomLogoutFilter가 처리합니다.")
				.security(java.util.List.of(new SecurityRequirement().addList("JWT")))
				.responses(new ApiResponses()
						.addApiResponse("200", new ApiResponse().description("성공"))
						.addApiResponse("401", new ApiResponse().description("인증 실패"))
				)
		);

		openAPI.path("/auth/logout", logoutPath);

		return openAPI;

	}
}
