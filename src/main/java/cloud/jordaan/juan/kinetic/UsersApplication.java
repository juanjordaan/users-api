package cloud.jordaan.juan.kinetic;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.config.EnableWebFlux;

import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.r2dbc.UserRepository;
import reactor.core.publisher.Flux;

@EnableWebFlux
@SpringBootApplication
public class UsersApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Bean
	ApplicationRunner init(UserRepository repository, DatabaseClient client) {
		return args -> {
			client.execute(
					"CREATE TABLE IF NOT EXISTS USER" + 
					"(ID BIGINT PRIMARY KEY auto_increment, USERNAME VARCHAR (255) NOT NULL, PASSWORD VARCHAR (255) NOT NULL, PHONE VARCHAR (255), UNIQUE KEY UNIQUE_KEY_USERNAME(USERNAME) );")
				.fetch()
				.first()
				.subscribe();

			client.execute("DELETE FROM USER").fetch().first().subscribe();

			Stream<User> stream = Stream.of(
				new User("admin"       , passwordEncoder.encode("admin")  , "0827064221"),
				new User("Juan Jordaan", passwordEncoder.encode("letmein"), "0827064221")
//				new User("Megan Jordaan", "letmein", "0827064221"), 
//				new User("Jemma Jordaan", "letmein", "0827064221")
				);

			repository.saveAll(Flux.fromStream(stream)).then().subscribe();
		};
	}
}
