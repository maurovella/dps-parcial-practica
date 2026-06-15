package edu.itba.class10.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "edu.itba.class10")
@ConfigurationPropertiesScan
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
