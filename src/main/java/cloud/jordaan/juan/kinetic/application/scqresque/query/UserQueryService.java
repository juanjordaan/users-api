package cloud.jordaan.juan.kinetic.application.scqresque.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.jordaan.juan.kinetic.application.scqresque.query.model.UserContactDetails;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.r2dbc.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Transactional
@Service
public class UserQueryService {
	static Logger logger = LoggerFactory.getLogger(UserQueryService.class);

	@Autowired
	private UserRepository userRepository;

	public Mono<User> findByUsername(String userName) {
		logger.info("findByUsername = " + userName);
		return userRepository.findByUsername(userName);
	}

	public Mono<User> getById(Long id) {
		logger.info("getById = " + id);
		return userRepository.findById(id);
	}

	public Mono<UserContactDetails> getUserContactDetails(Long id) {
		logger.info("getUserContactDetails = " + id);
		return Mono
			.from(userRepository.findById(id))
			.map(i -> {
				return new UserContactDetails(i.getId(), i.getUsername(), i.getPhone());
			});
	}

	public Flux<UserContactDetails> getAll() {
		logger.info("getAll");
		return userRepository.findAll()
			.map(i -> {
				return new UserContactDetails(i.getId(), i.getUsername(), i.getPhone());
			});
	}
}
