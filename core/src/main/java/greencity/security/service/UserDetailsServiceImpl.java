package greencity.security.service;

import greencity.security.model.CustomUserDetails;
import greencity.entity.User;
import greencity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    /**
     * Load a user by the email used as the username.
     *
     * @param email the user's email used as username
     * @return details of the user
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Autowired
    public UserDetailsServiceImpl(@Lazy UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new CustomUserDetails(
                user.getEmail(),
                user.getOwnSecurity().getPassword(),
                Collections.singletonList(authority),
                user.getId()
        );
    }
}
