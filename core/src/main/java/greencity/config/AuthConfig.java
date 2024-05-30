package greencity.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import greencity.controller.UserDetailsServiceImpl;
import greencity.repository.UserRepo;
import greencity.security.filters.AccessTokenAuthenticationFilter;
import greencity.security.jwt.JwtTool;
import greencity.security.providers.JwtAuthenticationProvider;
import greencity.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    private final AuthenticationManagerBuilder authenticationManager;

    @Autowired
    @Qualifier("userDetailsServiceBean")
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("passwordEncoderBean")
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthConfig(AuthenticationManagerBuilder authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean(name = "userDetailsServiceBean")
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepo);
    }

    @Bean(name = "passwordEncoderBean")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() throws Exception {
        UserDetailsService userDetailsService = userDetailsService();
        authenticationManager.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        authenticationManager.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }


    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance()).build();
    }

    @Bean
    public AccessTokenAuthenticationFilter accessTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AccessTokenAuthenticationFilter(jwtTool, authenticationManager, userService);
    }
}

