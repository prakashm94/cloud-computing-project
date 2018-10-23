package neu.coe.project.cloudapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableConfigurationProperties({
//        FileStorageProperties.class
//})

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
