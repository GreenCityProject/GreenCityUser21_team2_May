package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.TestConst;
import greencity.converters.UserArgumentResolver;
import greencity.dto.user.UserVO;
import greencity.entity.Language;
import greencity.entity.User;
import greencity.repository.UserRepo;
import greencity.service.FriendService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {
    private static final String friendLink = "/friends";
    private MockMvc mockMvc;

    @InjectMocks
    private FriendController friendController;
    @Mock
    private UserService userService;

    @Mock
    private FriendService friendService;
    @Mock
    private UserRepo userRepo;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(friendController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                        new UserArgumentResolver(userService, new ModelMapper()))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllFriendsOfUser() throws Exception{
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(TestConst.EMAIL);
        UserVO userVO = ModelUtils.getUserVO();
        when(userService.findByEmail(principal.getName())).thenReturn(userVO);

        int pageNumber = 1;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        mockMvc.perform(get( friendLink + "?page=1" )
                        .principal(principal))
                .andExpect(status().isOk());

        verify(friendService).getAllFriendsOfUser(userVO.getId(), pageable);
    }

    @Test
    @DisplayName("Delete non_Existing friend")
    void deleteFriendForUserTest() throws Exception{

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(TestConst.EMAIL);
        UserVO userVO = ModelUtils.getUserVO();
        when(userService.findByEmail(principal.getName())).thenReturn(userVO);

        mockMvc.perform(delete( friendLink + "/deleteFriend/" + 2L )
                        .principal(principal))
                .andExpect(status().isOk());
        verify(friendService).deleteFriendOfUser(userVO.getId(),2L);

    }

    @Test
    @DisplayName("Search friends by name")
    void searchFriendByName() throws Exception{

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(TestConst.EMAIL);
        UserVO userVO = ModelUtils.getUserVO();
        when(userService.findByEmail(principal.getName())).thenReturn(userVO);

        int pageNumber = 0;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        mockMvc.perform(get( friendLink + "/searchNewFriend/USB/false/false" )
                        .principal(principal))
                .andExpect(status().isOk());
        verify(friendService).searchNewFriend(userVO.getId(),"USB",null, false, pageable);

    }

}