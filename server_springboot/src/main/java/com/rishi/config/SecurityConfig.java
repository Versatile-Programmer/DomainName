package com.rishi.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Bean
	public DaoAuthenticationProvider authentication() {
		DaoAuthenticationProvider dao=new DaoAuthenticationProvider();
		dao.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		dao.setUserDetailsService(userDetailsService);
		return dao;

	}

	@Bean
    public RoleHierarchy roleHierarchy() {
     return RoleHierarchyImpl.
    		 withDefaultRolePrefix().
    		 role("HOD").implies("ARM").
    		 role("ARM").implies("DRM").
    		 build();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		return http
				.csrf(csrf->csrf.disable())
				.authorizeHttpRequests(req->req
						.requestMatchers("/register/**")
						.permitAll()
						.requestMatchers("/logout/**")
						.permitAll()
//						.requestMatchers("/login/*")
//						.hasRole("ARM")
						.requestMatchers("/drm/**")
						.hasRole("DRM")
						.requestMatchers("/arm/**")
						.hasRole("ARM")
						.requestMatchers("/domain/**")
						.hasRole("DRM")
						.anyRequest()
						.authenticated())
				.cors(cors->cors.configurationSource(corsConfig()))
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.sessionFixation()
						.changeSessionId())
				
				 .logout(logout -> logout
		                    .logoutUrl("/logout")  // Ensure Spring handles this URL
		                    .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)))
		                    .invalidateHttpSession(true)
		                    .clearAuthentication(true)
		                    .deleteCookies("JSESSIONID") // Remove JSESSIONID cookie
		                    .logoutSuccessHandler((request, response, authentication) -> {
		                        response.setStatus(HttpServletResponse.SC_OK);
		                    })
		                    )
				.build();
	}
	@Bean
	public CorsConfigurationSource corsConfig() {
		CorsConfiguration config=new CorsConfiguration();
		config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		config.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
		config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public CookieSerializer cookieSerializer() {
		 DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		    serializer.setCookiePath("/"); // Set the correct path
		    serializer.setCookieName("JSESSIONID");
		    serializer.setUseHttpOnlyCookie(true);
		    serializer.setSameSite("None"); // Set SameSite attribute for cross-origin requests
		    serializer.setUseSecureCookie(true);
		    return serializer;

	}
}
