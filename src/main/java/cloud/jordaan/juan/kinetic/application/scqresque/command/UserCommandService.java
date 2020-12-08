package cloud.jordaan.juan.kinetic.application.scqresque.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCommandResponse;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCreateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserContactUpdateCommand;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.r2dbc.UserRepository;
import reactor.core.publisher.Mono;

@Service
public class UserCommandService {
	static Logger logger = LoggerFactory.getLogger(UserCommandService.class);
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Mono<Void> deleteById(Long id) {
		logger.info("deleteById " + id);
		return repository.deleteById(id);
	}

	public Mono<UserCommandResponse> updateUser(Long id, UserContactUpdateCommand command) {
		logger.info("createUser " + command);
		return this.repository.findById(id)
			.switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found id " + id))))
			.map(i -> {
				i.setPhone(command.getPhone());
				return i;
			})
			.flatMap(j -> repository.save(j))
			.map(u -> new UserCommandResponse(u.getId(), u.getUsername(), u.getPhone()));
	}

	public Mono<UserCommandResponse> createUser(UserCreateCommand command) {
		logger.info("createUser " + command);
		return repository
				.save(new User(command.getUsername(), passwordEncoder.encode(command.getPassword()), command.getPhone()))
				.map( u -> new UserCommandResponse(u.getId(), u.getUsername(), u.getPhone()))
				;
	}
}
