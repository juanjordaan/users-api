package cloud.jordaan.juan.kinetic.infrastructure.persistence.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
	@Query("SELECT * FROM user WHERE username = :username")
	Mono<User> findByUsername(String username);
}
