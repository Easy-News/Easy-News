package shimp.easy_news;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EasyNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyNewsApplication.class, args);
	}

	   @PostConstruct
	   public void init() {
	       TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	   }
}
