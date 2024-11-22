package com.acousea.backend.core.users.infrastructure.entities.userData;


import com.acousea.backend.core.users.domain.constants.Gender;
import com.acousea.backend.core.users.domain.userData.UserInfo;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserInfoEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserInfoEntity(String firstName, String lastName, String email, String phoneNumber, LocalDateTime dateOfBirth, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public UserInfoEntity() {

    }

    public static UserInfoEntity fromDomain(UserInfo userInfo) {
        return new UserInfoEntity(
                userInfo.firstName(),
                userInfo.lastName(),
                userInfo.email(),
                userInfo.phoneNumber(),
                userInfo.dateOfBirth(),
                userInfo.gender()
        );
    }

    public UserInfo toDomain() {
        return new UserInfo(
                firstName,
                lastName,
                email,
                phoneNumber,
                dateOfBirth,
                gender
        );
    }
}
