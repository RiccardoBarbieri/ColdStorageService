package unibo.mapConfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapConfiguratorApplication {

	public static void main(String[] args) {

		System.setProperty("spring.profiles.active", "dev");

		SpringApplication.run(MapConfiguratorApplication.class, args);
	}

}
