package com.banu.mapper;

import com.banu.dto.request.RegisterRequestDto;
import com.banu.dto.request.UserCreateRequestDto;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.rabbitmq.model.RegisterMailModel;
import com.banu.rabbitmq.model.RegisterModel;
import com.banu.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    Auth fromRegisterRequestToAuth(RegisterRequestDto dto);

    RegisterResponseDto fromAuthToRegisterResponse(Auth auth);

    @Mapping(source = "id",target = "authId")
    UserCreateRequestDto fromAuthToCreateUserRequestDto(final Auth auth);

    @Mapping(source = "id",target = "authId")
    RegisterModel fromAuthToRegisterModel(Auth auth);

    RegisterMailModel fromAuthToRegisterMailModel(Auth auth);

}
