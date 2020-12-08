package cloud.jordaan.juan.kinetic.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
	private static final Logger logger = LoggerFactory.getLogger(SecurityContextRepository.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		return Mono
			.just(swe)
			.map(s -> s.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
			.filter(h -> {
				return h != null && h.startsWith(Jwt.BEARER_TOKEN_TYPE);
			})
			.switchIfEmpty(Mono.defer(() -> {
				logger.warn("couldn't find bearer string, will ignore the header.");
				return Mono.empty();
			}))
			.flatMap(authHeader -> {
				String authToken = authHeader.replace(Jwt.BEARER_TOKEN_TYPE, "");
				Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
				return this.authenticationManager.authenticate(auth).map((authentication) -> new SecurityContextImpl(authentication));
			})
			;
	}
}
