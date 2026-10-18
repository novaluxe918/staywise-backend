package com.hoainhi.staywise;

import io.jsonwebtoken.Jwts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class StaywiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaywiseApplication.class, args);
		String secret = Base64.getEncoder()
				.encodeToString(
						Jwts.SIG.HS256.key()
								.build()
								.getEncoded()
				);

		System.out.println(secret);
	}

}
