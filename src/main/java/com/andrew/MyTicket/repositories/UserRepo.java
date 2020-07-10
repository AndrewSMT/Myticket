package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByActivationCode(String code);
}
