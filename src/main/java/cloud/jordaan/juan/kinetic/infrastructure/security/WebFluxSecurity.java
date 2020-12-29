package cloud.jordaan.juan.kinetic.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurity {
	private static final String[] AUTH_WHITELIST = new String[] {"/login", "/register", "/logout", "/actuator/health**"};

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.csrf().disable()
			.authorizeExchange()
				.pathMatchers(AUTH_WHITELIST).permitAll()
				.anyExchange().authenticated()
			.and()
				.securityContextRepository(securityContextRepository)
				.authenticationManager(authenticationManager)
				.httpBasic()
			.and()
				.formLogin().disable()
					.exceptionHandling()
						.authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> { swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);}))
						.accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> { swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);}))
			.and()
				.logout().disable()
				.build();
	}

	@Bean
    public AuthenticationWebFilter authenticationWebFilter() {
	    AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
	    NegatedServerWebExchangeMatcher negateWhiteList = new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(AUTH_WHITELIST));
	    authenticationWebFilter.setRequiresAuthenticationMatcher(negateWhiteList);
	    authenticationWebFilter.setSecurityContextRepository(securityContextRepository);
//	    authenticationWebFilter.setAuthenticationFailureHandler(responseError());

	    return authenticationWebFilter;
	}

    /*private ServerAuthenticationFailureHandler responseError() {
    	return new ServerAuthenticationFailureHandler() {
			@Override
			public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
				webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	    		webFilterExchange.getExchange().getResponse().getHeaders().addIfAbsent(HttpHeaders.LOCATION,"/");
	    		return webFilterExchange.getExchange().getResponse().setComplete();
			}
		};
    }*/
}
