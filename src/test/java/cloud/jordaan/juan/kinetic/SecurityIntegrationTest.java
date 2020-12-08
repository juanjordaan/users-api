package cloud.jordaan.juan.kinetic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.AuthenticationCommand;
import cloud.jordaan.juan.kinetic.client.SecurityClient;
import cloud.jordaan.juan.kinetic.client.SecurityUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UsersApplication.class)
public class SecurityIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void whenGetFeaturesWithNoCredentials_thenReturns401() throws IOException {
		// when
		final ResponseEntity<String> responseEntity = restTemplate.exchange("/actuator", HttpMethod.GET, SecurityUtil.setAuthCookie(""), new ParameterizedTypeReference<String>() {}, new HashMap<>());

		// then
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void whenGetHealthWithCredentials_thenReturns200() throws IOException {
		// Given
		String cookie = SecurityUtil.getAuthCookie(SecurityClient.login(restTemplate, new AuthenticationCommand("admin", "admin")));

		// when
		final ResponseEntity<String> responseEntity = restTemplate.exchange("/actuator/health", HttpMethod.GET, SecurityUtil.setAuthCookie(cookie), new ParameterizedTypeReference<String>() {}, new HashMap<>());

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void whenGetInfoWithCredentials_thenReturns200() throws IOException {
		// Given
		String cookie = SecurityUtil.getAuthCookie(SecurityClient.login(restTemplate, new AuthenticationCommand("admin", "admin")));

		// when
		final ResponseEntity<String> responseEntity = restTemplate.exchange("/actuator/info", HttpMethod.GET, SecurityUtil.setAuthCookie(cookie), new ParameterizedTypeReference<String>() {}, new HashMap<>());

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void whenFeaturesWithCredentials_thenReturns200() throws IOException {
		// Given
		String cookie = SecurityUtil.getAuthCookie(SecurityClient.login(restTemplate, new AuthenticationCommand("admin", "admin")));

		// when
		final ResponseEntity<String> responseEntity = restTemplate.exchange("/actuator", HttpMethod.GET, SecurityUtil.setAuthCookie(cookie), new ParameterizedTypeReference<String>() {}, new HashMap<>());

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
}
