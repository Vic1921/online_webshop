package at.wst.online_webshop;

import at.wst.online_webshop.services.DBFiller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class OnlineWebshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineWebshopApplication.class, args);
	}

}
