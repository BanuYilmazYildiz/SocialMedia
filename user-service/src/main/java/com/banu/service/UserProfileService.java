package com.banu.service;

import com.banu.dto.request.UserCreateRequestDto;
import com.banu.dto.request.UserUpdateRequestDto;
import com.banu.exception.ErrorType;
import com.banu.exception.UserManagerException;
import com.banu.mapper.UserMapper;
import com.banu.repository.UserRepository;
import com.banu.repository.entity.UserProfile;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Status;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile,Long> {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    public UserProfileService(UserRepository userRepository, JwtTokenManager jwtTokenManager){
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Boolean createUser(UserCreateRequestDto dto) {
        try {
            save(UserMapper.INSTANCE.fromCreateRequest(dto));
            return true;
        }catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public Boolean activateStatus(Long authId) {
            Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId);
            if (userProfile.isEmpty()){
                throw new UserManagerException(ErrorType.USER_NOT_FOUND);
            }else {
                userProfile.get().setStatus(Status.ACTIVE);
                update(userProfile.get());
                return true;
            }
    }

    public Boolean updateUserProfile(UserUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbut(dto.getAbut());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        userRepository.save(userProfile.get());
        return true;
    }
}
