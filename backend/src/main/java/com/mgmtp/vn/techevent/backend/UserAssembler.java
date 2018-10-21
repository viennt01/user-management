package com.mgmtp.vn.techevent.backend;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class UserAssembler extends IdentifiableResourceAssemblerSupport<User, UserResource> {

	public UserAssembler() {
		super(UserController.class, UserResource.class);
	}

	@Override
	public UserResource toResource(final User entity) {
		return UserResource.builder()
				.username(entity.getUsername())
				.email(entity.getEmail())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.build();
	}

}
