package cloud.jordaan.juan.kinetic.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.AuthenticationCommand;

public interface SecurityClient {
	public static ResponseEntity<Void> login(TestRestTemplate restTemplate, AuthenticationCommand credentials) {
		Map<String, Long> m = new HashMap<>();
		return restTemplate.postForEntity("/login", credentials, Void.class, m);
	}
}
