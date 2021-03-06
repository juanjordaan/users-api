package cloud.jordaan.juan.kinetic.infrastructure.security;

import static cloud.jordaan.juan.kinetic.infrastructure.security.Jwt.AUTHORITIES_KEY;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

	@Autowired
	private TokenProvider tokenProvider;

	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication.getCredentials().toString())
			.switchIfEmpty(Mono.defer(() -> 
				Mono.error(new RuntimeException("Failed to extract token"))
			))
			.flatMap((authToken) -> {
				String username = tokenProvider.getUsernameFromToken(authToken);
				return Mono.just(new String [] { username, authToken });
			})
			.switchIfEmpty(Mono.defer(() -> 
				Mono.error(new RuntimeException("Failed to extract username from token"))
			))
			.filter((arr) -> {
				return !tokenProvider.isTokenExpired(arr[1]);
			})
			.switchIfEmpty(Mono.defer(() -> {
				logger.debug("Nothing to authenticate");
				return Mono.empty();
			}))
			.flatMap((arr) -> {
				Claims claims = tokenProvider.getAllClaimsFromToken(arr[1]);
				List<String> roles = claims.get(AUTHORITIES_KEY, List.class);
				List<SimpleGrantedAuthority> authorities = roles
					.stream()
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
				tokenProvider.refreshToken(arr[1]);
	            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(arr[0], arr[0], authorities);
	            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(arr[0], authorities));

	            logger.debug("Authenticated " + arr[0]);
	            return Mono.just(auth);
			})
			;
	}
	
	/*@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username;
		try {
			username = tokenProvider.getUsernameFromToken(authToken);
		} catch (Exception e) {
			logger.error(e.getMessage());
			username = null;
		}

		if (username != null && !tokenProvider.isTokenExpired(authToken)) {
			Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
			List<String> roles = claims.get(AUTHORITIES_KEY, List.class);
			List<SimpleGrantedAuthority> authorities = roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
			tokenProvider.refreshToken(authToken);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));

            logger.debug("Authenticated " + username);
            return Mono.just(auth);
		} else {
			logger.debug("Nothing to authenticate");
			return Mono.empty();
		}
	}*/
}
