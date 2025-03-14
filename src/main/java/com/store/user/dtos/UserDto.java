package com.store.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private AddressDto address;
    private String creditCard;
    private String creditCardType;
    private String username;
    private String password;
    private boolean enabled;
    private Set<AuthorityDto> authorities;
}
