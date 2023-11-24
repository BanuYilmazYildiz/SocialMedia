package com.banu.controller;

import com.banu.dto.request.ActivationRequestDto;
import com.banu.dto.request.LoginRequestDto;
import com.banu.dto.request.RegisterRequestDto;
import com.banu.dto.request.UpdateEmailOrUsernameRequestDto;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.repository.entity.Auth;
import com.banu.service.AuthService;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.banu.constants.RestApi.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    /*    login metodumuzu düzenleyelim.
        bize bir token üretip bu token'ı dönsün.
        ayrıca sadece status'u active olan kullanıcılar giriş yapabilsin.
    */


    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;


    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER+"2")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMQ(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerWithRabbitMQ(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody @Valid ActivationRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("/create-token")
    public ResponseEntity<String> createToken(Long authId, Role role){
        return ResponseEntity.ok(jwtTokenManager.createToken(authId,role).get());
    }

    @GetMapping("/create-token2")
    public ResponseEntity<String> createToken2(Long authId){
        return ResponseEntity.ok(jwtTokenManager.createToken(authId).get());
    }

    @GetMapping("/get-authId-from-token2")
    public ResponseEntity<Long> getAuthIdFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/get-role-from-token2")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }

    @PutMapping("/update_email_or_username")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto dto){
        return ResponseEntity.ok(authService.updateEmailOrUserName(dto));
    }

    @PutMapping(DELETEBYID + "/{id}")
    public ResponseEntity<Boolean> deleteAuth(@PathVariable  Long id){
        return ResponseEntity.ok(authService.deleteAuth(id));
    }

    @GetMapping("/redis")
    @Cacheable(value = "redisexample") //ön bellekleme yapabilmek için kullanılan anoasyon
    public String redisExample(@RequestParam String value){
        try {
            Thread.sleep(2000);
            return value;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @GetMapping("/deleteredis")
    @CacheEvict(cacheNames = "redisexample", allEntries = true) //nereden sileyim, hepsini mi sileyim
    public void redisDelete(){

    }

    @GetMapping("/deleteredis2")
    @CacheEvict(cacheNames = "redisexample", allEntries = true) //nereden sileyim, hepsini mi sileyim
    public Boolean redisDelete2(){
        try {
//            cacheManager.getCache("redisexample").clear(); //"redisexample etiketli bütün cacheleri temizler
            cacheManager.getCache("redisexample").evict("muhammet");
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestParam String role){
        return ResponseEntity.ok(authService.findByRole(role));
    }


}