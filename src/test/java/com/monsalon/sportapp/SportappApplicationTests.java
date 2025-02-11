package com.monsalon.sportapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SportappApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Test lancé avec succès !");
	}
}
