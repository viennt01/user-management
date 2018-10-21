package com.mgmtp.vn.techevent.backend;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication
public class UserManagementApplication extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				/**
				 * On local development, we run webpack-dev-server on http://localhost:9000 to server static resources
				 * Run Spring Boot as backend app on http://localhost:8080.
				 *
				 * We only allow http://localhost:9000 to send cross-origin requests!
				 *
				 */
				registry.addMapping("/api/**")
						.allowedOrigins("http://localhost:9000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
			}
		};
	}

	@Override
	public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getSource() instanceof ConfigurableWebApplicationContext) {
			log.info("\n\n" +
							"===============================================================================\n" +
							"\n" +
							"User Management started: http://localhost:{}{}\n" +
							"\n" +
							"===============================================================================\n",
					contextRefreshedEvent.getApplicationContext().getEnvironment().getProperty("server.port", Integer.class, 8080),
					contextRefreshedEvent.getApplicationContext().getEnvironment().getProperty("server.servlet.context-path", String.class, "")
			);
		}

	}

}
