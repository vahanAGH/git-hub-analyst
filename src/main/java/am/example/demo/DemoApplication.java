package am.example.demo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableEncryptableProperties
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext cap = SpringApplication.run(DemoApplication.class, args);
  }

}
