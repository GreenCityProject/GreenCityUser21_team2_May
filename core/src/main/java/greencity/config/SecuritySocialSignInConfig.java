package greencity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import static greencity.enums.IgnorePassword.IGNORE_PASSWORD;
import static greencity.enums.ValidateEmail.DO_NOT_VALIDATE_EMAIL;
import greencity.exception.exceptions.WrongEmailException;
import greencity.security.dto.SuccessSignInDto;
import greencity.security.dto.SuccessSignUpDto;
import greencity.security.dto.ownsecurity.OwnSignInDto;
import greencity.security.dto.ownsecurity.OwnSignUpDto;
import greencity.security.service.OwnSecurityService;
import greencity.service.UserService;
import java.io.PrintWriter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecuritySocialSignInConfig {

    private final UserService userService;
    private final OwnSecurityService ownSecurityService;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
            Map<String, Object> claims = principal.getIdToken().getClaims();

            String email = (String) claims.get("email");
            if (isUserExist(email)) {
                OwnSignInDto ownSignInDto = OwnSignInDto.builder()
                    .email(email)
                    .build();
                SuccessSignInDto successSignInDto = ownSecurityService.signIn(ownSignInDto, IGNORE_PASSWORD);

                response.setStatus(200);
                try (PrintWriter writer = response.getWriter()) {
                    writer.write(new ObjectMapper().writeValueAsString(successSignInDto));
                }
            } else {
                OwnSignUpDto ownSignUpDto = OwnSignUpDto.builder()
                    .email(email)
                    .name((String) claims.get("name"))
                    .password(randomAlphanumeric(16))//Password needed for a database. Ignored when user signIn.
                    .isUbs(false)
                    .build();
                SuccessSignUpDto successSignUpDto = ownSecurityService
                    .signUp(ownSignUpDto, DO_NOT_VALIDATE_EMAIL, "ua");

                response.setStatus(201);
                try (PrintWriter writer = response.getWriter()) {
                    writer.write(new ObjectMapper().writeValueAsString(successSignUpDto));
                }
            }
        };
    }

    private boolean isUserExist(String email) {
        try {
            userService.findByEmail(email);
        } catch (WrongEmailException e) {
            return false;
        }
        return true;
    }
}
