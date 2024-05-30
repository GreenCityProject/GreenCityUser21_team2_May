package greencity.security.filters;

import greencity.dto.user.UserVO;
import greencity.security.jwt.JwtTool;
import greencity.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccessTokenAuthenticationFilterTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    JwtTool jwtTool;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserService userService;

    @InjectMocks
    private AccessTokenAuthenticationFilter authenticationFilter;

    private final ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(systemOutContent));
    }

    @Test
    void whenDoFilterInternalUsingCookies_thenCorrect() throws IOException, ServletException {
        String testToken = "testToken";
        Cookie[] cookies = new Cookie[]{ new Cookie("accessToken", testToken) };

        when(request.getCookies()).thenReturn(cookies);
        when(request.getRequestURI()).thenReturn("/management/testUri");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testToken, "")))
                .thenReturn(new UsernamePasswordAuthenticationToken("test@mail.com", null));
        when(userService.findNotDeactivatedByEmail("test@mail.com"))
                .thenReturn(Optional.of(new UserVO()));

        authenticationFilter.doFilterInternal(request, response, chain);

        verify(authenticationManager).authenticate(any());
        verify(chain).doFilter(request, response);
    }

    @Test
    void whenDoFilterInternalNoToken_thenFilterChainDoFilterCalled() throws ServletException, IOException {
        when(request.getCookies()).thenReturn(null);

        authenticationFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void whenDoFilterInternalTokenExpired_thenCorrectLogMessage() throws ServletException, IOException {
        String expiredToken = "expiredToken";
        when(jwtTool.getTokenFromHttpServletRequest(request)).thenReturn(expiredToken);
        when(authenticationManager.authenticate(any())).thenThrow(ExpiredJwtException.class);

        authenticationFilter.doFilterInternal(request, response, chain);

        assertTrue(systemOutContent.toString().contains("Token has expired: " + expiredToken));
    }

    @Test
    void whenDoFilterInternalNonManagementPath_thenTokenNotExtractedFromCookies() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/nonManagement/testUri");

        authenticationFilter.doFilterInternal(request, response, chain);

        verify(jwtTool).getTokenFromHttpServletRequest(request);
    }
}