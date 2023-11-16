package com.banu.controller;

import com.banu.dto.request.UserCreateRequestDto;
import com.banu.dto.request.UserUpdateRequestDto;
import com.banu.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.banu.constants.RestApi.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(ACTIVATESTATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUserProfile(@RequestBody UserUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateUserProfile(dto));
    }

}
