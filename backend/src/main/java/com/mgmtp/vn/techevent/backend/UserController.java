package com.mgmtp.vn.techevent.backend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = "application/hal+json")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

	private final UserService userService;
	private final UserAssembler userAssembler;

	@GetMapping
	public Resources<UserResource> allUsers() {
		List<User> users = userService.allUsers();
		Resources<UserResource> resources = new Resources<>(userAssembler.toResources(users));
		resources.add(linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
		return resources;
	}

	@GetMapping("/{id}")
	public UserResource getUser(@PathVariable final String id) {
		User user = userService.getUser(id);
		return userAssembler.toResource(user);
	}


}
