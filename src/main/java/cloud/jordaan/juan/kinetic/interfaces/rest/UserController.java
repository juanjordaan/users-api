package cloud.jordaan.juan.kinetic.interfaces.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.jordaan.juan.kinetic.application.scqresque.command.UserCommandService;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCommandResponse;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserContactUpdateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.command.model.UserCreateCommand;
import cloud.jordaan.juan.kinetic.application.scqresque.query.UserQueryService;
import cloud.jordaan.juan.kinetic.application.scqresque.query.model.UserContactDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(Controllers.USERS_URL)
public class UserController {
	@Autowired
	private UserQueryService queryService;

	@Autowired
	private UserCommandService commandService;

//	@PreAuthorize("hasRole('Guest')")
	@GetMapping
	public Flux<UserContactDetails> getAllUsers() {
		return queryService.getAll();
	}

//	@PreAuthorize("hasRole('Guest')")
	@GetMapping("/{id}/contact")
	public Mono<UserContactDetails> getUserContactDetails(@PathVariable("id") Long id) {
		return queryService.getUserContactDetails(id);
	}

//	@PreAuthorize("hasRole('Guest')")
	@PostMapping
	public Mono<UserCommandResponse> createUser(@Valid @RequestBody UserCreateCommand user) {
		return commandService.createUser(user);
	}

	@PutMapping("/{id}")
	public Mono<UserCommandResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserContactUpdateCommand user) {
		return commandService.updateUser(id, user);
	}

//	@PreAuthorize("hasRole('Guest')")
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return commandService.deleteById(id);
    }
}
