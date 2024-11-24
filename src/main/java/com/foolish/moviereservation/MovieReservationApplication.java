package com.foolish.moviereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableJpaRepositories("com.foolish.moviereservation.repository")
@EntityScan("com.foolish.moviereservation.model")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class MovieReservationApplication {

  public static void main(String[] args) {
    SpringApplication.run(MovieReservationApplication.class, args);
  }

}
