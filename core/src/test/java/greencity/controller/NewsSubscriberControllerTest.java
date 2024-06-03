package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.service.NewsSubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class NewsSubscriberControllerTest {

    private static final String LINK = "/subscriber";

    private MockMvc mockMvc;

    @Mock
    private NewsSubscriberService subscriberService;

    @InjectMocks
    private NewsSubscriberController newsSubscriberController;

    static final String EMAIL = "test@gmail.com";
    static final String TOKEN = "token";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(newsSubscriberController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void subscribeTest() throws Exception {
        NewsSubscriberRequestDto newsSubscriberRequestDto = NewsSubscriberRequestDto.builder().email(EMAIL).build();

        mockMvc.perform(post(LINK + "/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newsSubscriberRequestDto)))
                        .andExpect(status().isCreated());

        verify(subscriberService).subscribe(newsSubscriberRequestDto);
    }

    @Test
    void getAllSubscribersTest() throws Exception {
        NewsSubscriberResponseDto newsSubscriberResponseDto = NewsSubscriberResponseDto.builder().email(EMAIL).unsubscribeToken(TOKEN).build();
        List<NewsSubscriberResponseDto> dtoList = List.of(newsSubscriberResponseDto);

        when(subscriberService.getAll()).thenReturn(dtoList);

        mockMvc.perform(get(LINK)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0]").value(newsSubscriberResponseDto));

        verify(subscriberService).getAll();
    }

    @Test
    void unsubscribeTest() throws Exception {
        mockMvc.perform(get(LINK + "/unsubscribe")
                        .param("email", EMAIL)
                        .param("unsubscribeToken", TOKEN))
                        .andExpect(status().isOk());

        verify(subscriberService).unsubscribe(EMAIL, TOKEN);
    }
}