package com.croche.lariharumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.croche.lariharumi.models.User.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
