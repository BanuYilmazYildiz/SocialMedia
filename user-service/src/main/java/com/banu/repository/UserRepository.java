package com.banu.repository;

import com.banu.repository.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile,Long> {


    Optional<UserProfile> findOptionalByAuthId(Long authId);
}
