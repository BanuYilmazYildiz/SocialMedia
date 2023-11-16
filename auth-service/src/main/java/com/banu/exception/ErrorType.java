package com.banu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_ERROR(5100, "Sunu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    USERNAME_DUBLICATE(4111, "Lullanıcı adı kullanılmaktadır", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112, "Kullanıcı bulunamadı", HttpStatus.BAD_REQUEST),
    ACTIVATION_CODE_ERROR(4113, "Aktivasyon kodu hatalıdır", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4114, "Geçersiz token", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4100, "Kullanıcı adı veya şifre hatalı", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(4115,"Hesabınız aktive değildir. Lütfen hesabınızı aktif hale getirin" ,HttpStatus.BAD_REQUEST ),
    TOKEN_NOT_CREATED(4116,"Token oluşturulamadı" ,HttpStatus.BAD_REQUEST );


    private int code;

    private String message;

    private HttpStatus httpStatus;
}
