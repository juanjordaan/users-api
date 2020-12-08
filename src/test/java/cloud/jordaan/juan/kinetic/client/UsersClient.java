package cloud.jordaan.juan.kinetic.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCommandResponse;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCreateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.query.model.UserContactDetails;
import cloud.jordaan.juan.kinetic.interfaces.rest.Controllers;

public interface UsersClient {
	public static ResponseEntity<UserCommandResponse> create(TestRestTemplate restTemplate, String cookie, UserCreateCommand command) {
		return restTemplate.exchange(Controllers.USERS_URL, HttpMethod.POST, SecurityUtil.setAuthCookie(command, cookie), new ParameterizedTypeReference<UserCommandResponse>() {}, new HashMap<>());
	}

	public static ResponseEntity<List<UserContactDetails>>list(TestRestTemplate restTemplate, String cookie) {
		return restTemplate.exchange(Controllers.USERS_URL, HttpMethod.GET, SecurityUtil.setAuthCookie(cookie) , new ParameterizedTypeReference<List<UserContactDetails>>() {}, new HashMap<>());
	}
}
