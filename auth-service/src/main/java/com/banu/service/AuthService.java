package com.banu.service;

import com.banu.dto.request.*;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.exception.AuthServiceException;
import com.banu.exception.ErrorType;
import com.banu.manager.UserManager;
import com.banu.mapper.AuthMapper;
import com.banu.rabbitmq.producer.RegisterMailProducer;
import com.banu.rabbitmq.producer.RegisterProducer;
import com.banu.repository.AuthRepository;
import com.banu.repository.entity.Auth;
import com.banu.utility.CodeGenerator;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Role;
import com.banu.utility.enums.Status;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final AuthRepository authRepository;
    private final UserManager userManager;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;

    private final RegisterMailProducer registerMailProducer;


    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager jwtTokenManager, CacheManager cacheManager, RegisterProducer registerProducer, RegisterMailProducer registerMailProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.registerMailProducer = registerMailProducer;
    }

    @Transactional //hata oldu yapılan işlemler geri alınsın
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        try {
            save(auth);
            userManager.createUser(AuthMapper.INSTANCE.fromAuthToCreateUserRequestDto(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e) {
            //delete(auth);
            throw new AuthServiceException(ErrorType.USER_NOT_CREATED);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    @Transactional //hata oldu yapılan işlemler geri alınsın
    public RegisterResponseDto registerWithRabbitMQ(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        try {
            save(auth);
            registerProducer.sendNewUser(AuthMapper.INSTANCE.fromAuthToRegisterModel(auth));
            registerMailProducer.sendActivationCode(AuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e) {
            //delete(auth);
            throw new AuthServiceException(ErrorType.USER_NOT_CREATED);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.LOGIN_ERROR, "Kullanıcı adı veya sifre hatali.......");
        }
        if (!auth.get().getStatus().equals(Status.ACTIVE)) {
            throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        Optional<String> token = jwtTokenManager.createToken(auth.get().getId(), auth.get().getRole());
        //if (token.isEmpty()){
        //    throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
        //}
        return jwtTokenManager.createToken(auth.get().getId(), auth.get().getRole()).orElseThrow(() -> {
            throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
        });
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getActivationCode().equals(dto.getActivationCode())) {
            auth.get().setStatus(Status.ACTIVE);
            update(auth.get());
            userManager.activateStatus(auth.get().getId());
            return true;
        } else {
            throw new AuthServiceException(ErrorType.ACTIVATION_CODE_ERROR);
        }
    }

    public Boolean updateEmailOrUserName(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return true;
    }

    public Boolean deleteAuth(Long id) {
        Optional<Auth> auth = authRepository.findById(id);
        if (auth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(Status.DELETED);
        update(auth.get());
        DeleteUserProfileRequestDto dto = DeleteUserProfileRequestDto.builder()
                .authId(auth.get().getId())
                .build();
        userManager.deleteUserProfile(dto);
        return true;
    }


    public List<Long> findByRole(String role) {
        Role myRole;
        try {
            myRole = Role.valueOf(role.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new AuthServiceException(ErrorType.ROLE_NOT_FOUND);
        }
        return authRepository.findAllByRole(myRole).stream().map(Auth::getId).collect(Collectors.toList());
    }
}
