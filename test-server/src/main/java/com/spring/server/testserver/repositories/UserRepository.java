package com.spring.server.testserver.repositories;

import com.spring.server.testserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
