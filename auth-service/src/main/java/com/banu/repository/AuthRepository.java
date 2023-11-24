package com.banu.repository;

import com.banu.repository.entity.Auth;
import com.banu.utility.enums.Role;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findByUsernameAndPassword(String username, String password);


    Optional<Auth> findByUsername(String username);

    List<Auth> findAllByRole(Role role);
}
