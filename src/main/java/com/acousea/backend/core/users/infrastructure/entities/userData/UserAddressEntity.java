package com.acousea.backend.core.users.infrastructure.entities;


import com.acousea.backend.core.users.domain.userData.UserAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserAddressEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    public UserAddressEntity(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getters, setters, and constructors

    public static UserAddressEntity fromDomain(UserAddress address) {
        return new UserAddressEntity(
                address.street(),
                address.city(),
                address.state(),
                address.postalCode(),
                address.country()
        );
    }

    public UserAddress toDomain() {
        return new UserAddress(
                street,
                city,
                state,
                postalCode,
                country
        );
    }
}

