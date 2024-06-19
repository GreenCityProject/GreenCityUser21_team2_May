package greencity.controller;

import greencity.config.SecurityConfig;
import greencity.repository.UserRepo;
import greencity.security.controller.OwnSecurityController;
import greencity.security.jwt.JwtTool;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {SecurityConfig.class, JwtTool.class})
@WebMvcTest(OwnSecurityController.class)
class UserSecuredControllerTest {

    static final String USER_LINK = "/user";
    static final String ROLE_USER = "USER";
    static final String ROLE_ADMIN = "ADMIN";

    @MockBean
    UserService userService;

    @MockBean
    UserRepo userRepo;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @DisplayName("Test response status for user get endpoint as unauthenticated user")
    void getUser_EndpointResponse_StatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(USER_LINK))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test response status for user get endpoint as authenticated USER")
    @WithMockUser(roles = ROLE_USER)
    void getUser_EndpointResponse_StatusIsForbidden() throws Exception {
        mockMvc.perform(get(USER_LINK))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test response status for user is online endpoint as unauthenticated user")
    void userIsOnline_EndpointResponse_StatusIsUnauthorized() throws Exception {
        mockMvc.perform(patch(USER_LINK + "/deleteProfilePicture"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test response status for user patch profilePicture as unauthenticated user")
    void userProfilePicture_EndpointResponse_StatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(USER_LINK))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Test response status for user is online endpoint as unauthenticated user")
    void userIsOnline_EndpointResponse_StatusIsUnauthorized() throws Exception {
        mockMvc.perform(get(USER_LINK + "/isOnline/{userId}/", 1L))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test response status for user deleteProfilePicture endpoint as authenticated USER")
    @WithMockUser(roles = ROLE_USER)
    void userIsOnline_EndpointResponse_StatusIsOk() throws Exception {
        mockMvc.perform(patch(USER_LINK + "/deleteProfilePicture"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test response status for user patch profilePicture as authenticated ADMIN with valid data")
    @WithMockUser(roles = ROLE_ADMIN)
    void userProfilePicture_EndpointResponse_StatusIsNotFound() throws Exception {
        String base64 = "your_base64_string";

        MockMultipartFile image =
            new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(USER_LINK + "/profilePicture")
            .file(image)
            .param("base64", base64)
            .with(request -> {
                request.setMethod("PATCH");
                return request;
            }))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test response status for user deleteProfilePicture endpoint as authenticated ADMIN")
    @WithMockUser(roles = ROLE_ADMIN)
    void userIsOnline_EndpointResponse_StatusIsNotFound() throws Exception {
        mockMvc.perform(patch(USER_LINK + "/deleteProfilePicture"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test response status for user is online endpoint as authenticated USER")
    @WithMockUser(roles = ROLE_USER)
    void userIsOnline_EndpointResponse_StatusIsForbidden() throws Exception {
        mockMvc.perform(get(USER_LINK + "/isOnline/{userId}/", 1L))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test response status for user get endpoint as authenticated ADMIN")
    @WithMockUser(roles = ROLE_ADMIN)
    void userIsOnline_EndpointResponse_StatusIsNotFound() throws Exception {
        mockMvc.perform(get(USER_LINK))
                .andExpect(status().isNotFound());
    }
  
    @Test
    @DisplayName("Test response status for user is online endpoint as authenticated ADMIN")
    @WithMockUser(roles = ROLE_ADMIN)
    void userIsOnline_EndpointResponse_StatusIsNotFound() throws Exception {
        mockMvc.perform(get(USER_LINK + "/isOnline/{userId}/", 1L))
            .andExpect(status().isNotFound());
    }
}