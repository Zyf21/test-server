package com.spring.server.testserver.services.implement;

import com.spring.server.testserver.domain.User;
import com.spring.server.testserver.error.ErrorCodes;
import com.spring.server.testserver.error.RestException;
import com.spring.server.testserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl {


	private final UserRepository userRepository;
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getUsers() {
		 return userRepository.findAll();

	}


	public User getUserById(Long userId) {

		return  userRepository.findById(userId)
				.orElseThrow(() -> new RestException(ErrorCodes.FILE_NOT_FOUND));
	}


	public void deleteUser(Long userId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new RestException(ErrorCodes.FILE_NOT_FOUND));

//		if ((!requesterId.equals(userId)) || (!Arrays.asList(userFromDB.getGrantedAuthorities()).contains(UserRoles.ROLE_ADMIN.name()))) {
//			throw new RestException(ErrorCodes.USER_PERMISSION_DENIED);
//		}

		userRepository.deleteById(userId);

	}
}
