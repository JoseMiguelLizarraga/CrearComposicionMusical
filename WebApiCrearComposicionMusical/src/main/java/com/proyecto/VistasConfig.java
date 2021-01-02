package com.proyecto;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class VistasConfig implements WebMvcConfigurer 
{
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) 
	{
        registry.addViewController("/").setViewName("index");
        
        // Hacer que spring boot se encargue del enrutamiento de las vistas (En este caso Angular 8)
        
        registry.addViewController("/login").setViewName("index");
        registry.addViewController("/composicionMusical/**").setViewName("index");
	}
}
