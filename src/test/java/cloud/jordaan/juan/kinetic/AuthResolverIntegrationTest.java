package cloud.jordaan.juan.kinetic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.AuthenticationCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.query.model.UserContactDetails;
import cloud.jordaan.juan.kinetic.client.SecurityClient;
import cloud.jordaan.juan.kinetic.client.SecurityUtil;
import cloud.jordaan.juan.kinetic.client.UsersClient;
import cloud.jordaan.juan.kinetic.interfaces.rest.Controllers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UsersApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthResolverIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	private WebTestClient testClient;

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void givenUserCredential_whenHome_thenExpect401Status() {
		testClient
			.get()
			.uri(Controllers.USERS_URL)
			.exchange()
			.expectStatus()
			.isUnauthorized();
	}

	@Test
	public void givenCredential_whenUsersList_thenExpectOk() {
		// Given
		AuthenticationCommand credentials = new AuthenticationCommand("admin", "admin");
		String cookie = SecurityUtil.getAuthCookie(SecurityClient.login(restTemplate, credentials));

		// when
		ResponseEntity<List<UserContactDetails>> response = UsersClient.list(restTemplate, cookie);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void givenUserCredentials_whenLogin_thenExpectOKandCookie() {
		// given
		AuthenticationCommand credentials = new AuthenticationCommand("admin", "admin");

		// when
		ResponseEntity<Void> response = SecurityClient.login(restTemplate, credentials);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		String cookie = SecurityUtil.getAuthCookie(response);
		assertNotNull(cookie);
	}

	@Test
	public void givenWrongUserCredentials_whenLogin_thenExpectUnauthorizedAndNoCookie() {
		// given
		AuthenticationCommand credentials = new AuthenticationCommand("admin", "nimda");

		// when
		ResponseEntity<Void> response = SecurityClient.login(restTemplate, credentials);

		// then
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		String cookie = SecurityUtil.getAuthCookie(response);
		assertNull(cookie);
	}
}
