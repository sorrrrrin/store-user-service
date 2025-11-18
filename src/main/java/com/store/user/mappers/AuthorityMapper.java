package com.store.user.mappers;

import com.store.user.dtos.AuthorityDto;
import com.store.user.dtos.AuthorityResponseDto;
import com.store.user.entities.Authority;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    AuthorityDto authorityToAuthorityDto(Authority authority);

    Authority authorityDtoToAuthority(AuthorityDto authorityDto);

    AuthorityResponseDto authorityToAuthorityResponseDto(Authority authority);

    Authority authorityResponseDtoToAuthority(AuthorityResponseDto authorityResponseDto);

}