package unibo.springSSG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class SsgAppl {

	public static void main(String[] args) {

		Properties properties = System.getProperties();
		System.out.println("________________Properties: " + properties.getProperty("activeProfile"));

		SpringApplication.run(SsgAppl.class, args);
	}

}
