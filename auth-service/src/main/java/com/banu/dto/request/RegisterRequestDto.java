package com.banu.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequestDto {

    @NotNull
    @NotBlank
    @Size(min = 5,max = 20, message = "Kullanıcı adı en az 5 en fazla 20 karakterden oluşmalıdır")
    private String username;

    @Email
    private String email;

    @Size(min = 8,max = 32, message = "Şifre minimum 8 maksimim 32 karakterli olmalıdır")
    @NotBlank
    private String password;
}
