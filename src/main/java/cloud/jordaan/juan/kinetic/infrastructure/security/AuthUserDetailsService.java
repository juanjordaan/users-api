package cloud.jordaan.juan.kinetic.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cloud.jordaan.juan.kinetic.infrastructure.persistence.entity.User;
import cloud.jordaan.juan.kinetic.infrastructure.persistence.r2dbc.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Service
public class AuthUserDetailsService implements ReactiveUserDetailsService {
	static Logger logger = LoggerFactory.getLogger(AuthUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

    public Flux<UserDetails> findUsers() {
    	logger.info("findUsers");
		return userRepository
        	.findAll()
    		.switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
    		.map( i -> mapToUserDetails(i))
    		;
    }

	@Override
    public Mono<UserDetails> findByUsername(String username) {
		logger.info("findByUsername " + username);
		return userRepository
        	.findByUsername(username)
    		.switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
    		.map( i -> mapToUserDetails(i))
    		;
    }

	private UserDetails mapToUserDetails(User user) {
		logger.info("mapToUserDetails " + user);
		return org.springframework.security.core.userdetails.User
	            .withUsername(user.getUsername())
	            .password(user.getPassword())
	            .authorities("Guest")
	            .build();
	}
}
