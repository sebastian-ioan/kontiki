package org.vizuina;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
public class KontikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KontikiApplication.class, args);
	}

    @Configuration
    @ApplicationPath("/api")
    @ImportResource("classpath:spring-security.xml")
    public class JerseyConfig extends ResourceConfig {
        public JerseyConfig() {
            register(KontikiController.class);
        }
    }

}
