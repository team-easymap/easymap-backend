package com.easymap.easymap;

import com.easymap.easymap.entity.User;
import com.easymap.easymap.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@AllArgsConstructor
public class EasymapApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasymapApplication.class, args);
	}

}
