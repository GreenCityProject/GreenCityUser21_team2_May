package greencity.controller;


import greencity.config.SecurityConfig;
import greencity.repository.UserRepo;
import greencity.security.jwt.JwtTool;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {SecurityConfig.class, JwtTool.class})
@WebMvcTest
class UserControllerSecurityTest {

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
    @DisplayName("USER trying to get into PUT users/{id} (403 Forbidden expected)")
    @WithMockUser(roles = ROLE_USER)
    void updateUserManagementTestUser() throws Exception {
        mockMvc.perform(put("/user/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN trying to get into PUT users/{id} (200 OK expected)")
    @WithMockUser(roles = ROLE_ADMIN)
    void updateUserManagementTestAdmin() throws Exception {
        /// when ...

        mockMvc.perform(put("/user/{id}", 3L))
                .andExpect(status().isOk());
    }
}