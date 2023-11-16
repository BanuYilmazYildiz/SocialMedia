package com.banu.dto.request;

import com.banu.utility.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private String phone;

    private String avatarUrl;

    private String address;

    private String abut;

}
