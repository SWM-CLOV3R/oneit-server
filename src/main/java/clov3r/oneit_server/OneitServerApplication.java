package clov3r.oneit_server;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OneitServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OneitServerApplication.class, args);

    }

}
