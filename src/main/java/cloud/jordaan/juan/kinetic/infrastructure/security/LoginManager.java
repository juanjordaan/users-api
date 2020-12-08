package cloud.jordaan.juan.kinetic.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cloud.jordaan.juan.kinetic.application.scqresque.command.model.AuthenticationCommand;
import reactor.core.publisher.Mono;

@Component
public class LoginManager {
	private static Logger logger = LoggerFactory.getLogger(LoginManager.class);
	@Autowired
	private ReactiveUserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider jwtProvider;

	@Value("${custom.session.expire.minutes:3}")
	private Long expiryMinutes;

	public Mono<ResponseEntity<Void>> login(AuthenticationCommand credentials) {
		logger.info("Logging in user " + credentials.getUsername());
		return userDetailsService
			.findByUsername(credentials.getUsername())
			.filter(i -> {
				return passwordEncoder.matches(credentials.getPassword(), i.getPassword());
			})
			.map(i -> {
				String jwt = jwtProvider.generateToken(i);
				ResponseCookie cookie = ResponseCookie
					.fromClientResponse(Jwt.XAUTH_TOKEN_TYPE, jwt)
					.maxAge(expiryMinutes)
					.httpOnly(true)
					.path("/")
					.secure(false)
					.build();

				return ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, cookie.toString())
					.<Void>build();
			})
			.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build()))
			;
	}
}
