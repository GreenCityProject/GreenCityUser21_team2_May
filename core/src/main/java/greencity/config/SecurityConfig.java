package greencity.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import greencity.security.filters.AccessTokenAuthenticationFilter;
import greencity.security.jwt.JwtTool;
import greencity.security.providers.JwtAuthenticationProvider;
import greencity.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;
import java.util.Collections;
import static greencity.constant.AppConstant.*;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Config for security.
 *
 * @author Nazar Stasyuk && Yurii Koval
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalAuthentication
public class SecurityConfig {
    private JwtTool jwtTool;
    private UserService userService;
    private ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManagerBuilder authenticationManager;
    private static final String USER_LINK = "/user";

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public SecurityConfig(
            JwtTool jwtTool,
            UserService userService,
            AuthenticationManagerBuilder authenticationManager,
            @Qualifier("passwordEncoderBean") PasswordEncoder passwordEncoder,
            @Qualifier("userDetailsServiceBean") UserDetailsService userDetailsService
    ) {
        this.jwtTool = jwtTool;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method for configure security.
     *
     * @param http {@link HttpSecurity}
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4205"));
            config.setAllowedMethods(
                    Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
            config.setAllowedHeaders(
                    Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                            "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L);
            return config;
            }))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(
                        new AccessTokenAuthenticationFilter(jwtTool, authenticationManager(), userService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, resp, exc) -> resp.sendError(
                                SC_UNAUTHORIZED, "Authorize first."))
                        .accessDeniedHandler((req, resp, exc) -> resp.sendError(
                                SC_FORBIDDEN, "You don't have authorities.")))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/static/css/**", "/static/img/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/v2/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger.json",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/ownSecurity/verifyEmail",
                                "/ownSecurity/updateAccessToken",
                                "/ownSecurity/restorePassword",
                                "/googleSecurity",
                                "/facebookSecurity/generateFacebookAuthorizeURL",
                                "/facebookSecurity/facebook",
                                "/user/activatedUsersAmount",
                                "/user/{userId}/habit/assign",
                                "/token",
                                "/socket/**",
                                "/user/findAllByEmailNotification",
                                "/user/checkByUuid",
                                "/user/get-user-rating")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/ownSecurity/signUp",
                                "/ownSecurity/signIn",
                                "/ownSecurity/updatePassword")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, USER_LINK,
                                "/user/shopping-list-items/habits/{habitId}/shopping-list",
                                "/user/{userId}/{habitId}/custom-shopping-list-items/available",
                                "/user/{userId}/profile/", "/user/isOnline/{userId}/",
                                "/user/{userId}/profileStatistics/",
                                "/user/userAndSixFriendsWithOnlineStatus",
                                "/user/userAndAllFriendsWithOnlineStatus",
                                "/user/findByIdForAchievement",
                                "/user/findNotDeactivatedByEmail",
                                "/user/findByEmail",
                                "/user/findIdByEmail",
                                "/user/findAllUsersCities",
                                "/user/findById",
                                "/user/findUserByName/**",
                                "/user/findByUuId",
                                "/user/findUuidByEmail",
                                "/user/lang",
                                "/user/createUbsRecord",
                                "/user/{userId}/sixUserFriends/",
                                "/ownSecurity/password-status",
                                "/user/emailNotifications")
                        .hasAnyRole(USER, ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.POST, USER_LINK,
                                "/user/shopping-list-items",
                                "/user/{userId}/habit",
                                "/ownSecurity/set-password",
                                "/email/sendReport",
                                "/email/sendHabitNotification",
                                "/email/addEcoNews",
                                "/email/changePlaceStatus",
                                "/email/general/notification")
                        .hasAnyRole(USER, ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.PUT,
                                "/ownSecurity/changePassword",
                                "/user/profile",
                                "/user/{id}/updateUserLastActivityTime/{date}",
                                "/user/language/{languageId}",
                                "/user/employee-email")
                        .hasAnyRole(USER, ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.PUT,
                                "/user/edit-authorities",
                                "/user/authorities",
                                "/user/deactivate-employee",
                                "/user/markUserAsDeactivated",
                                "/user/markUserAsActivated")
                        .hasAnyRole(ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.GET,
                                "/user/get-all-authorities",
                                "/user/get-positions-authorities",
                                "/user/get-employee-login-positions")
                        .hasAnyRole(ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.PATCH,
                                "/user/shopping-list-items/{userShoppingListItemId}",
                                "/user/profilePicture",
                                "/user/deleteProfilePicture")
                        .hasAnyRole(USER, ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.DELETE,
                                "/user/shopping-list-items/user-shopping-list-items",
                                "/user/shopping-list-items")
                        .hasAnyRole(USER, ADMIN, UBS_EMPLOYEE, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.GET,
                                "/user/all",
                                "/user/roles",
                                "/user/findUserForManagement",
                                "/user/searchBy",
                                "/user/findAll")
                        .hasAnyRole(ADMIN, MODERATOR, EMPLOYEE)
                        .requestMatchers(HttpMethod.POST,
                                "/ownSecurity/sign-up-employee")
                        .hasAnyRole(UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.POST,
                                "/user/filter",
                                "/ownSecurity/register")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.PATCH,
                                "/user/status",
                                "/user/role",
                                "/user/update/role")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, "/management/login")
                        // .not().fullyAuthenticated()
                        .rememberMe()
                        .requestMatchers(HttpMethod.GET, "/management/login")
                        .permitAll()
                        .requestMatchers("/css/**", "/img/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/user-rating")
                        .hasAnyRole(ADMIN, MODERATOR, EMPLOYEE, UBS_EMPLOYEE, USER)
                        .anyRequest().hasAnyRole(ADMIN));
        return http.build();
    }

    @PostConstruct
    public void initAuthenticationManager() throws Exception {
        authenticationManager.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        authenticationManager.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }

    /**
     * Method for configure type of authentication provider.
     *
     * @param auth {@link AuthenticationManagerBuilder}
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }

    /**
     * Bean {@link GoogleIdTokenVerifier} that uses in verify googleIdToken.
     */
    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance()).build();
    }

    @Bean
    public AccessTokenAuthenticationFilter accessTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AccessTokenAuthenticationFilter(jwtTool, authenticationManager, userService);
    }

    /**
     * Provides AuthenticationManager.
     *
     * @return {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationConfiguration authenticationConfiguration = applicationContext.getBean(AuthenticationConfiguration.class);
        return authenticationConfiguration.getAuthenticationManager();
    }
}