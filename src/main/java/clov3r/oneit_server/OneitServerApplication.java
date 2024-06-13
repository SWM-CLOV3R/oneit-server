package clov3r.oneit_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OneitServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneitServerApplication.class, args);
    }

}
