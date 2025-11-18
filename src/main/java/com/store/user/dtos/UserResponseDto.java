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
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private AddressDto address;
    private String username;
    private boolean enabled;
    private Set<AuthorityResponseDto> authorities;
}
