package com.store.user.utils;

import com.store.user.dtos.AddressDto;
import com.store.user.dtos.AuthorityDto;
import com.store.user.dtos.UserDto;
import com.store.user.entities.Address;
import com.store.user.entities.Authority;
import com.store.user.entities.User;

public class TestUtils {
    public static User getUser() {
        return User.builder()
                .id(TestConstants.USER_ID)
                .email(TestConstants.USER_EMAIL)
                .username(TestConstants.USER_USERNAME)
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

    public static Authority getAuthority() {
        return Authority.builder()
                .id(TestConstants.AUTHORITY_ID)
                .authority(TestConstants.AUTHORITY_AUTHORITY)
                .build();
    }

    public static AuthorityDto getAuthorityDto() {
        return AuthorityDto.builder()
                .id(TestConstants.AUTHORITY_ID)
                .authority(TestConstants.AUTHORITY_AUTHORITY)
                .build();
    }

}
