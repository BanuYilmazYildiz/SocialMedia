package com.banu.service;

import com.banu.dto.request.DeleteUserProfileRequestDto;
import com.banu.dto.request.UpdateEmailOrUsernameRequestDto;
import com.banu.dto.request.UserCreateRequestDto;
import com.banu.dto.request.UserUpdateRequestDto;
import com.banu.exception.ErrorType;
import com.banu.exception.UserManagerException;
import com.banu.manager.AuthManager;
import com.banu.mapper.UserMapper;
import com.banu.rabbitmq.model.RegisterModel;
import com.banu.repository.UserRepository;
import com.banu.repository.entity.UserProfile;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Status;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AuthManager authManager;
    private final CacheManager cacheManager;

    public UserProfileService(UserRepository userRepository, JwtTokenManager jwtTokenManager, AuthManager authManager, CacheManager cacheManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
    }

    public Boolean createUser(UserCreateRequestDto dto) {
        try {
            save(UserMapper.INSTANCE.fromCreateRequest(dto));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        } else {
            userProfile.get().setStatus(Status.ACTIVE);
            update(userProfile.get());
            return true;
        }
    }


    public Boolean updateUserProfile(UserUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername());

        if (!userProfile.get().getUsername().equals(dto.getUsername()) || !userProfile.get().getEmail().equals(dto.getEmail())) {
            userProfile.get().setUsername(dto.getUsername());
            userProfile.get().setEmail(dto.getEmail());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = UpdateEmailOrUsernameRequestDto.builder()
                    .username(userProfile.get().getUsername())
                    .email(userProfile.get().getEmail())
                    .id(userProfile.get().getAuthId())
                    .build();

            authManager.update(updateEmailOrUsernameRequestDto);
        }
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        update(userProfile.get());
        return true;
    }


    public Boolean deleteUserProfile(DeleteUserProfileRequestDto dto) {
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.DELETED);
        update(userProfile.get());
        return true;
    }
    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username)  {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile = userRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole", key = "#role.toUpperCase()")
    public List<UserProfile> findByRole(@RequestParam String role){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Long> authIds = authManager.findByRole(role).getBody();//response entity içindeki body'yi alıyoruz
        return authIds.stream().map(x-> userRepository.findOptionalByAuthId(x)
                .orElseThrow(()-> {throw new UserManagerException(ErrorType.USER_NOT_FOUND);})).collect(Collectors.toList());
    }

    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            save(UserMapper.INSTANCE.fromRegisterModelToUserProfile(model));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
}

