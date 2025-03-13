package com.store.user.utils;

import com.store.user.dtos.AddressDto;
import com.store.user.dtos.UserDto;
import com.store.user.entities.Address;
import com.store.user.entities.User;

public class TestUtils {
    public static User getUser() {
        return User.builder()
                .id(TestConstants.USER_ID)
                .email(TestConstants.USER_EMAIL)
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(TestConstants.USER_ID)
                .email(TestConstants.USER_EMAIL)
                .build();
    }

    public static Address getAddress() {
        return Address.builder()
                .street(TestConstants.ADDRESS_STREET)
                .build();
    }

    public static AddressDto getAddressDto() {
        return AddressDto.builder()
                .street(TestConstants.ADDRESS_STREET)
                .build();
    }

}
