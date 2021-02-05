package com.spring.server.testserver.services.interfaces;

import com.spring.server.testserver.domain.User;

import java.util.List;

public interface UserService {

	List<User> getUsers();

	User getUserById(Long userId);

	//void addUser(UserInDTO user);

	//void updateUser(Long requesterId, Long userId, UserInDTO user);

	void deleteUser(Long requesterId, Long userId);
}
