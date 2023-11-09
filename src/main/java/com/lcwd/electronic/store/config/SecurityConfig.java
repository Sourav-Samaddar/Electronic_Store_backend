package com.lcwd.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.security.JwtAuthenticationFilter;
import com.lcwd.electronic.store.services.impl.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private static final String[] publicURLs = {
			"/auth/**",
			"/v3/api-docs/**",
			"/v2/api-docs/**",
			"/swagger-resources/**",
			"/swagger-ui/**",
			"/webjars/**",
			"/test/**",
			"/users/image/**",
			"/swagger-ui.html"
			};
	
	@Autowired
	private CustomUserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails normal = User.builder()
//				.username("sourav")
//				.password(passwordEncoder().encode("livecurious"))
//				.roles("NORMAL")
//				.build();
//		
//		UserDetails admin = User.builder()
//				.username("ritu")
//				.password(passwordEncoder().encode("ritu"))
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(normal,admin);
//	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.disable())
		.authorizeHttpRequests(auth -> auth.requestMatchers(publicURLs).permitAll());
		http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/products/**").permitAll());
		http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
				.anyRequest().authenticated());
				
		http.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler));
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.authenticationProvider(daoAuthenticationProvider());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsServiceImpl);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	// CORS Configuration
    @Bean
    public FilterRegistrationBean corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:4200"));
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
        filterRegistrationBean.setOrder(-110);
        return filterRegistrationBean;


    }
	
}
