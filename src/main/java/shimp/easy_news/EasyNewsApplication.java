package shimp.easy_news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EasyNewsApplication {
	// push to baepo
	public static void main(String[] args) {
		SpringApplication.run(EasyNewsApplication.class, args);
	}

}
