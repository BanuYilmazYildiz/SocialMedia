package com.banu.manager;

import com.banu.dto.request.DeleteUserProfileRequestDto;
import com.banu.dto.request.UserCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.banu.constants.RestApi.ACTIVATESTATUS;
import static com.banu.constants.RestApi.DELETEBYID;


@FeignClient(url = "http://localhost:7071/api/v1/user",name = "auth-userprofile")
public interface UserManager {

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto);

    @GetMapping(ACTIVATESTATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @PutMapping(DELETEBYID)
    public ResponseEntity<Boolean> deleteUserProfile(@RequestBody DeleteUserProfileRequestDto dto);
}
