package com.practica.usersapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;

//@SpringBootApplication
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class PracticaUsersapiApplication extends SpringBootServletInitializer{
	
	@Autowired
	private JdbcTemplate template;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PracticaUsersapiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PracticaUsersapiApplication.class, args);
    }

	public void run(String... args) throws Exception {
		template.execute("DROP TABLE IF EXISTS users CASCADE;");
		template.execute("DROP TABLE IF EXISTS phone CASCADE;");
		template.execute("CREATE DATABASE testdb");
		template.execute(
		    "CREATE TABLE testdb.users ("
		    + "id INTEGER PRIMARY KEY auto_increment, "
		    + "name VARCHAR(50) NOT NULL, "
		    + "email VARCHAR(50) NOT NULL, "
		    + "password VARCHAR(50) NOT NULL, "
		    + "created DATE NOT NULL, "
		    + "modified DATE NOT NULL, "
		    + "lastLogin DATE NOT NULL, "
		    + "token VARCHAR(150) NOT NULL, "
		    + "isActive BOOLEAN NOT NULL)"
		);
		template.execute(
		    "CREATE TABLE testdb.phone ("
		    + "id INTEGER PRIMARY KEY auto_increment, "
		    + "number VARCHAR(15) NOT NULL, "
		    + "citycode VARCHAR(5) NOT NULL, "
		    + "countrycode VARCHAR(5) NOT NULL)"
		);
		
	}
}
