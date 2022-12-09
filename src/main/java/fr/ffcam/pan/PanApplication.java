package fr.ffcam.pan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;


@EntityScan
@SpringBootApplication
@EnableSpringDataWebSupport
@EnableScheduling
@EnableJpaRepositories
public class PanApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PanApplication.class, args);
	}
}
