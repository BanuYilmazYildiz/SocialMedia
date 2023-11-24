package com.banu.repository;

import com.banu.repository.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserProfile,String> {


    Optional<UserProfile> findOptionalByAuthId(Long authId);

    Optional<UserProfile> findOptionalByUsernameIgnoreCase(String username);
}
