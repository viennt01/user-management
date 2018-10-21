package com.mgmtp.vn.techevent.backend;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Data
@Builder
@Relation(collectionRelation = "users")
public class UserResource extends ResourceSupport {

	private String username;
	private String email;
	private String firstName;
	private String lastName;

}
