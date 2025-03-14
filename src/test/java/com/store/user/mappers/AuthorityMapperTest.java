package com.store.user.mappers;

import com.store.user.dtos.AuthorityDto;
import com.store.user.entities.Authority;
import com.store.user.utils.TestConstants;
import com.store.user.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class AuthorityMapperTest {

    private final AuthorityMapper authorityMapper = new AuthorityMapperImpl();

    @Test
    void authorityToAuthorityDtoTest() {
        Authority authority = TestUtils.getAuthority();

        AuthorityDto authorityDto = authorityMapper.authorityToAuthorityDto(authority);

        assertNotNull(authorityDto);
        assertEquals(TestConstants.AUTHORITY_ID, authorityDto.getId());
        assertEquals(TestConstants.AUTHORITY_AUTHORITY, authorityDto.getAuthority());
    }

    @Test
    void authorityToAuthorityDto_NullAuthority_ReturnsNullTest() {
        AuthorityDto authorityDto = authorityMapper.authorityToAuthorityDto(null);
        assertNull(authorityDto);
    }

    @Test
    void authorityDtoToAuthorityTest() {
        AuthorityDto authorityDto = TestUtils.getAuthorityDto();

        Authority authority = authorityMapper.authorityDtoToAuthority(authorityDto);

        assertNotNull(authority);
        assertEquals(TestConstants.AUTHORITY_ID, authority.getId());
        assertEquals(TestConstants.AUTHORITY_AUTHORITY, authority.getAuthority());
    }

    @Test
    void authorityDtoToAuthority_NullAuthorityDto_ReturnsNullTest() {
        Authority authority = authorityMapper.authorityDtoToAuthority(null);
        assertNull(authority);
    }
}