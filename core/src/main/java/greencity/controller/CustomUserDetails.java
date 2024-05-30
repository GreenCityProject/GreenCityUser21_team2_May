package greencity.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final Long id;
    private String email;

    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(email, password, authorities);
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
