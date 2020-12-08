package cloud.jordaan.juan.kinetic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;

import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import cloud.jordaan.juan.kinetic.interfaces.rest.Controllers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UsersApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationTest extends AbstractIntegrationTest {
	private static final String USERS_URL = BASE_URL + "/users";

	@Autowired
	private WebTestClient testClient;

	@Test
	@Ignore
	@WithMockUser
	public void whenHasMockCredentials_thenStatusOk() {
		this.testClient
			.get()
			.uri(Controllers.USERS_URL)
			.exchange()
			.expectStatus()
			.isOk();
	}

	@Test
	public void test_whenCreateUser_thenCorrectUser() {

		User user = new User("Employee 1 Name", "secret", "0987654321");

		createTestUser(user)
			.expectStatus()
				.isOk()
			.expectBody(User.class)
				.value((u) -> {
					assertNotNull(u.getId());
					assertEquals(u.getUsername(), user.getUsername());
					assertEquals(u.getPassword(), user.getPassword());
					assertEquals(u.getPhone(), user.getPhone());
				});
	}

	@Test
	public void test_whenUpdateUser_thenCorrectUser() {

		User user = new User("Employee 1 Name", "secret", "0987654321");

		createTestUser(user)
			.expectStatus()
				.isOk()
			.expectBody(User.class)
				.value((u) -> {
					assertNotNull(u.getId());
					assertEquals(u.getUsername(), user.getUsername());
					assertEquals(u.getPassword(), user.getPassword());
					assertEquals(u.getPhone(), user.getPhone());
				});
	}

	public ResponseSpec createTestUser(User user) {
		return testClient
				.post()
					.uri(USERS_URL)
					.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".getBytes()))
					.body(BodyInserters.fromObject(user))
				.exchange();
	}

	public ResponseSpec updateTestUser(User user) {
		return testClient
				.put()
					.uri(USERS_URL)
					.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".getBytes()))
					.bodyValue(user)
				.exchange();
	}
}
