package com.banu.service;

import com.banu.dto.request.ActivationRequestDto;
import com.banu.dto.request.LoginRequestDto;
import com.banu.dto.request.RegisterRequestDto;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.exception.AuthServiceException;
import com.banu.exception.ErrorType;
import com.banu.manager.UserManager;
import com.banu.mapper.AuthMapper;
import com.banu.repository.AuthRepository;
import com.banu.repository.entity.Auth;
import com.banu.utility.CodeGenerator;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.ServiceManager;
import com.banu.utility.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final AuthRepository authRepository;
    private final UserManager userManager;
    private final JwtTokenManager jwtTokenManager;




    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager jwtTokenManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        userManager.createUser(AuthMapper.INSTANCE.fromAuthToCreateUserRequestDto(auth));
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    public String login(LoginRequestDto dto) {
       Optional<Auth> auth = authRepository.findByUsernameAndPassword(dto.getUsername(),dto.getPassword());
       if (auth.isEmpty()){
           throw new AuthServiceException(ErrorType.LOGIN_ERROR,"Kullanıcı adı veya sifre hatali.......");
       }
       if (!auth.get().getStatus().equals(Status.ACTIVE)){
           throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
       }
       Optional<String> token = jwtTokenManager.createToken(auth.get().getId(),auth.get().getRole());
       //if (token.isEmpty()){
       //    throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
       //}
        return jwtTokenManager.createToken(auth.get().getId(),auth.get().getRole()).orElseThrow(()->{
            throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED);
        });
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty()){
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getActivationCode().equals(dto.getActivationCode())){
            auth.get().setStatus(Status.ACTIVE);
            update(auth.get());
            userManager.activateStatus(auth.get().getId());
            return true;
        }else {
            throw new AuthServiceException(ErrorType.ACTIVATION_CODE_ERROR);
        }
    }
}
