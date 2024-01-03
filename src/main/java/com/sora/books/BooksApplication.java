package com.sora.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackageClasses = {
	BooksApplication.class,
	Jsr310JpaConverters.class
})
public class BooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowCredentials(false)
					.allowedOrigins("*")
					.allowedMethods(
						HttpMethod.POST.name(),
						HttpMethod.GET.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name()
					)
					.allowedHeaders("*");
			}
		};
	}
}
