package fr.nilpo.pan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableSpringDataWebSupport
@EnableScheduling
@EnableJpaRepositories
public class PanApplication {
	
	public static void main(String[] args) {
		// Liquibase (4.30+) otherwise contacts config.liquibase.com at every
		// startup to decide whether to send usage analytics. Can still be
		// overridden with -Dliquibase.analytics.enabled=... or the
		// LIQUIBASE_ANALYTICS_ENABLED environment variable.
		if (System.getProperty("liquibase.analytics.enabled") == null
				&& System.getenv("LIQUIBASE_ANALYTICS_ENABLED") == null) {
			System.setProperty("liquibase.analytics.enabled", "false");
		}
		
		SpringApplication.run(PanApplication.class, args);
	}
}
