package cloud.jordaan.juan.kinetic.interfaces.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cloud.jordaan.juan.kinetic.application.scqresque.command.UserCommandService;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.AuthenticationCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCommandResponse;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCreateCommand;
import cloud.jordaan.juan.kinetic.infrastructure.security.LoginManager;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class AuthenticationController {
	@Autowired
	private UserCommandService commandService;
	@Autowired
	private LoginManager loginManager;

	@PostMapping("/register")
	public Mono<UserCommandResponse> register(@Valid @RequestBody UserCreateCommand command) {
		return commandService.createUser(command);
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<Void>> login(@RequestBody AuthenticationCommand credentials, ServerHttpResponse response) {
		return loginManager.login(credentials);
	}
}
