package greencity.security.service;

import greencity.entity.OwnSecurity;
import greencity.security.model.CustomUserDetails;
import greencity.entity.User;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static greencity.enums.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {
    private UserRepo userRepo;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        userRepo = Mockito.mock(UserRepo.class);
        userDetailsService = new UserDetailsServiceImpl(userRepo);
    }

    @Test
    void loadUserByUsernameTest() {
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        mockUser.setRole(ROLE_USER);

        OwnSecurity mockOwnSecurity = new OwnSecurity();
        mockOwnSecurity.setPassword("testPassword");
        mockUser.setOwnSecurity(mockOwnSecurity);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userDetails.getAuthorities());
    }

    @Test
    void loadUserByUsername_NotFoundTest() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@test.com");
        });
    }
}