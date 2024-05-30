package greencity.config.bean;

import greencity.security.service.UserDetailsServiceImpl;
import greencity.repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsServiceConfig {

    private final UserRepo userRepo;

    public UserDetailsServiceConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean(name = "userDetailsServiceBean")
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepo);
    }
}
