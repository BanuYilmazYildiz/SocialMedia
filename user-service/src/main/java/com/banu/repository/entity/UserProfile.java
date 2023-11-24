package com.banu.repository.entity;

import com.banu.utility.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserProfile extends BaseEntity{

    @Id
    private String id;

    private Long authId;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private String address;

    private String about;

    @Builder.Default
    private Status status=Status.PENDING;


}
