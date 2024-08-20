package clov3r.oneit_server;

import clov3r.oneit_server.config.security.JwtTokenProvider;
import clov3r.oneit_server.config.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OneitServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OneitServerApplication.class, args);

    }

}
