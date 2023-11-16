package com.banu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_ERROR(5200, "Sunu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4200, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    USERNAME_DUBLICATE(4210,"Lullanıcı adı kullanılmaktadır", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4211,"Kullanıcı bulunamadı", HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4212,"Kullanıcı profili oluşturulamadı" ,HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4213, "Geçersiz token", HttpStatus.BAD_REQUEST);


    private int code;

    private String message;

    private HttpStatus httpStatus;
}
