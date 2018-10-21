package com.mgmtp.vn.techevent.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "public")
@RequiredArgsConstructor
public class User implements Identifiable<Serializable> {

	@Id
	@Getter
	@Setter
	private String id = UUID.randomUUID().toString();

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private String firstName;

	@Getter
	@Setter
	private String lastName;

	@Getter
	@Setter
	private String password;

}
