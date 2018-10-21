package com.mgmtp.vn.techevent.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

	private final UserRepository userRepository;

	public List<User> allUsers() {
		return userRepository.findAll();

	}

	public User getUser(final String id) {
		Optional<User> invoice = userRepository.findById(id);
		return invoice.orElseThrow(() ->
				new ResourceNotFoundException(MessageFormat.format("Could not find user with id {0}", id)));
	}
}
