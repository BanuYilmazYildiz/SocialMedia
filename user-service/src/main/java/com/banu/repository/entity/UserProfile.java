package com.banu.repository.entity;

import com.banu.utility.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserProfile extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authId;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private String address;

    private String abut;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;


}
