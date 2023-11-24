package com.banu.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequestDto {

    private String token;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private String address;

    private String about;

}
