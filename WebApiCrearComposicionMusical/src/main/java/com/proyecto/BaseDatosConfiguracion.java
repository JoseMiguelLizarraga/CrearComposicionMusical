package com.proyecto;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;


@Configuration
public class BaseDatosConfiguracion
{
	
	@Autowired DataSource dataSource;   // Comentar esto en caso de usar el metodo   dataSource()  de esta clase

	@Autowired
	JpaVendorAdapter jpaVendorAdapter;
	
	
	@SuppressWarnings("unchecked")
	@Bean
	public Jackson2ObjectMapperBuilder configureObjectMapper()  // Impedir error could not initialize proxy en jackson. Esto es cuando en una instancia, el atributo padre es null
	{
	    return new Jackson2ObjectMapperBuilder().modulesToInstall(Hibernate5Module.class);
	}

	
	/*
	@Bean(name="dataSource")
	public DataSource dataSource()     
	{	
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/sistema_ventas_2019?serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("");  
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        return new HikariDataSource(config);
	} 
	*/

	@Bean
	@Primary
	public EntityManagerFactory entityManagerFactory() 
	{
	    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
	    emf.setDataSource(dataSource);
	    //emf.setDataSource(this.dataSource());
	    emf.setPackagesToScan(new String[] { "com.proyecto.model.entity" }); 
	    emf.setJpaVendorAdapter(jpaVendorAdapter);
	    emf.afterPropertiesSet();
	    return emf.getObject();
	}
	
	@Bean
	public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
	    return entityManagerFactory.unwrap(SessionFactory.class);
	}


}