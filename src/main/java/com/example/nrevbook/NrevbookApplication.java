package com.example.nrevbook;

import com.example.nrevbook.model.Role;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class NrevbookApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(NrevbookApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (!userRepository.existsByUsername("admin")) {
			var admin = new User();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(encoder.encode("Admin@123"));
			admin.getRoles().add(Role.ROLE_ADMIN);
			userRepository.save(admin);
			System.out.println("Created default admin user");
		}
	}
}

