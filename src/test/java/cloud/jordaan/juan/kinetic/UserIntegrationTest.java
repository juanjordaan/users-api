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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCommandResponse;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserContactUpdateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCreateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.query.model.UserContactDetails;
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
	@Ignore
	@WithMockUser
	public void test_whenCreateUser_thenCorrectUser() {

		UserCreateCommand user = new UserCreateCommand("Employee 2 Name", "secret", "0987654321");

		createTestUser(user)
			.expectStatus()
				.isOk()
			.expectBody(UserCommandResponse.class)
				.value((u) -> {
					assertNotNull(u.getId());
					assertEquals(u.getUsername(), user.getUsername());
					assertEquals(u.getPhone(), user.getPhone());
				});
	}

	@Test
	@Ignore
	@WithMockUser
	public void test_whenUpdateContact_thenCorrectUser() {

		UserCreateCommand user = new UserCreateCommand("Employee 1 Name", "secret", "0987654321");

		createTestUser(user)
			.expectStatus()
				.isOk()
			.expectBody(UserContactDetails.class)
				.value((u) -> {
					assertNotNull(u.getId());
					assertEquals(u.getUsername(), user.getUsername());
					assertEquals(u.getPhone(), user.getPhone());
				});
	}

	public ResponseSpec createTestUser(UserCreateCommand user) {
		return testClient
				.post()
					.uri(USERS_URL)
					.body(BodyInserters.fromValue(user))
				.exchange();
	}

	public ResponseSpec updateTestUser(UserContactUpdateCommand user) {
		return testClient
				.put()
					.uri(USERS_URL+"/1")
					.bodyValue(user)
				.exchange();
	}
}
