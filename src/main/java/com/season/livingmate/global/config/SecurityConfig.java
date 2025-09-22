package com.season.livingmate.global.config;

import java.util.Collections;

import com.season.livingmate.global.auth.application.RefreshTokenService;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import com.season.livingmate.global.auth.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ObjectMapper objectMapper;
	private final RefreshTokenService refreshTokenService;
	//private final AuthService authService;
	private final UserRepository userRepository;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager, CustomLogoutFilter customLogoutFilter, JwtBlacklistFilter jwtBlacklistFilter) throws Exception {
		LoginFilter loginFilter = new LoginFilter(authManager,  jwtProvider, objectMapper, refreshTokenService);
		loginFilter.setFilterProcessesUrl("/auth/login");

		http
			.csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화, jwt 방식은 csrf에 대한 공격을 방어하지 않아도 됨
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 설정

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll() // 로그인 회원가입
						.requestMatchers("/posts/**", "/profile/**").authenticated() // 게시글 인증 필요
						.anyRequest().permitAll()
				)

			.cors(cors -> cors
				.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOriginPatterns(Collections.singletonList("*"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Collections.singletonList("Authorization"));
						config.setMaxAge(3600L);
						return config;
					}
				}))

			 .addFilterBefore(customLogoutFilter, LogoutFilter.class)
			 .addFilterBefore(jwtAuthenticationFilter, LoginFilter.class)
			 .addFilterBefore(jwtBlacklistFilter, JwtAuthenticationFilter.class)
			 .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
