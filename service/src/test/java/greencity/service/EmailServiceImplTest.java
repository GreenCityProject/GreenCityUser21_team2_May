package greencity.service;

import greencity.ModelUtils;
import static greencity.ModelUtils.getUser;
import greencity.TestConst;
import greencity.dto.category.CategoryDto;
import greencity.dto.econews.AddEcoNewsDtoResponse;
import greencity.dto.econews.EcoNewsForSendEmailDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.dto.notification.NotificationDto;
import greencity.dto.place.PlaceNotificationDto;
import greencity.dto.user.PlaceAuthorDto;
import greencity.dto.user.UserActivationDto;
import greencity.dto.user.UserDeactivationReasonDto;
import greencity.dto.violation.UserViolationMailDto;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.Executors;

import static greencity.ModelUtils.getUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static greencity.ModelUtils.getUser;

class EmailServiceImplTest {
    private EmailService service;
    private PlaceAuthorDto placeAuthorDto;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private ITemplateEngine templateEngine;
    @Mock
    private UserRepo userRepo;

    @BeforeEach
    public void setup() {
        initMocks(this);
        service = new EmailServiceImpl(javaMailSender, templateEngine, userRepo, Executors.newCachedThreadPool(),
            "http://localhost:4200", "http://localhost:4200", "http://localhost:8080",
            "test@email.com");
        placeAuthorDto = PlaceAuthorDto.builder()
            .id(1L)
            .email("testEmail@gmail.com")
            .name("testName")
            .build();
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @Test
    void sendChangePlaceStatusEmailTest() {
        when(userRepo.existsUserByEmail(TestConst.EMAIL)).thenReturn(true);

        String authorFirstName = "test author first name";
        String placeName = "test place name";
        String placeStatus = "test place status";
        String authorEmail = TestConst.EMAIL;
        service.sendChangePlaceStatusEmail(authorFirstName, placeName, placeStatus, authorEmail);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    @DisplayName("Test for checking the response status of the endpoint /email/changePlaceStatus with invalid data")
    void emailChangePlaceStatusEmail_EndpointResponse_StatusIsNotFound() throws Exception {
        when(userRepo.existsUserByEmail(TestConst.EMAIL)).thenReturn(false);

        String authorFirstName = "test author first name";
        String placeName = "test place name";
        String placeStatus = "test place status";
        String authorEmail = TestConst.EMAIL;

        assertThrows(NotFoundException.class,
            () -> service.sendChangePlaceStatusEmail(authorFirstName, placeName, placeStatus, authorEmail));
    }

    @Test
    void sendAddedNewPlacesReportEmailTest() {
        CategoryDto testCategory = CategoryDto.builder().name("CategoryName").build();
        PlaceNotificationDto testPlace1 =
            PlaceNotificationDto.builder().name("PlaceName1").category(testCategory).build();
        PlaceNotificationDto testPlace2 =
            PlaceNotificationDto.builder().name("PlaceName2").category(testCategory).build();
        Map<CategoryDto, List<PlaceNotificationDto>> categoriesWithPlacesTest = new HashMap<>();
        categoriesWithPlacesTest.put(testCategory, Arrays.asList(testPlace1, testPlace2));
        service.sendAddedNewPlacesReportEmail(
            Collections.singletonList(placeAuthorDto), categoriesWithPlacesTest, "DAILY");
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendCreatedNewsForAuthorTest() {
        when(userRepo.findByEmail("test@gmail.com")).thenReturn(Optional.of(ModelUtils.getUser()));

        EcoNewsForSendEmailDto dto = new EcoNewsForSendEmailDto();
        PlaceAuthorDto placeAuthorDto = new PlaceAuthorDto();
        placeAuthorDto.setEmail("test@gmail.com");
        dto.setAuthor(placeAuthorDto);
        service.sendCreatedNewsForAuthor(dto);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendNewNewsForSubscriber() {
        List<NewsSubscriberResponseDto> newsSubscriberResponseDtos =
            Collections.singletonList(new NewsSubscriberResponseDto("test@gmail.com", "someUnsubscribeToken"));
        AddEcoNewsDtoResponse addEcoNewsDtoResponse = ModelUtils.getAddEcoNewsDtoResponse();
        service.sendNewNewsForSubscriber(newsSubscriberResponseDtos, addEcoNewsDtoResponse);
        verify(javaMailSender).createMimeMessage();
    }

    @ParameterizedTest
    @CsvSource(value = {"1, Test, test@gmail.com, token, ru",
        "1, Test, test@gmail.com, token, ua",
        "1, Test, test@gmail.com, token, en"})
    void sendVerificationEmail(Long id, String name, String email, String token, String language) {
        service.sendVerificationEmail(id, name, email, token, language, false);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendVerificationEmailIllegalStateException() {
        assertThrows(IllegalStateException.class,
            () -> service.sendVerificationEmail(1L, "Test", "test@gmail.com", "token", "enuaru", false));
    }

    @Test
    void sendApprovalEmail() {
        service.sendApprovalEmail(1L, "userName", "test@gmail.com", "someToken");
        verify(javaMailSender).createMimeMessage();
    }

    @ParameterizedTest
    @CsvSource(value = {"1, Test, test@gmail.com, token, ru, true",
        "1, Test, test@gmail.com, token, ua, false",
        "1, Test, test@gmail.com, token, en, false"})
    void sendRestoreEmail(Long id, String name, String email, String token, String language, Boolean isUbs) {
        service.sendRestoreEmail(id, name, email, token, language, isUbs);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendRestoreEmailIllegalStateException() {
        assertThrows(IllegalStateException.class,
            () -> service.sendRestoreEmail(1L, "Test", "test@gmail.com", "token", "enuaru", false));
    }

//    @Test
//    @DisplayName("Test sendHabitNotification method when user exists")
//    void sendHabitNotification_userExists_sendsEmail() {
//        when(userRepo.existsUserByEmail("taras@gmail.com")).thenReturn(true);
//
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//        service.sendHabitNotification(TestConst.NAME, TestConst.EMAIL);
//
//        verify(javaMailSender).createMimeMessage();
//        verify(javaMailSender).send(mimeMessage);
//    }

    @Test
    @DisplayName("Test sendHabitNotification method when user not found")
    void sendHabitNotification_userNotFound_throwsNotFoundException() {
        when(userRepo.existsUserByEmail(TestConst.EMAIL)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.sendHabitNotification(TestConst.NAME, TestConst.EMAIL));
        verify(userRepo).existsUserByEmail(any());
        verify(userRepo, never()).findByEmail(any());
        verify(javaMailSender, never()).createMimeMessage();
    }

    @Test
    void sendReasonOfDeactivation() {
        List<String> test = List.of("test", "test");
        UserDeactivationReasonDto test1 = UserDeactivationReasonDto.builder()
            .deactivationReasons(test)
            .lang("en")
            .email("test@ukr.net")
            .name("test")
            .build();
        service.sendReasonOfDeactivation(test1);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendMessageOfActivation() {
        List<String> test = List.of("test", "test");
        UserActivationDto test1 = UserActivationDto.builder()
            .lang("en")
            .email("test@ukr.net")
            .name("test")
            .build();
        service.sendMessageOfActivation(test1);
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    @DisplayName("Test for sending user violation email when user doesn't exist")
    void sendUserViolationEmail_userNotFound_throwsNotFoundException() {
        UserViolationMailDto dto = ModelUtils.getUserViolationMailDto();
        when(userRepo.existsUserByEmail(dto.getEmail())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.sendUserViolationEmail(dto));

        verify(javaMailSender, never()).createMimeMessage();
    }

    @Test
    @DisplayName("Test for sending user violation email when user exists")
    void sendUserViolationEmail_userExists_sendsEmail() {
        UserViolationMailDto dto = ModelUtils.getUserViolationMailDto();
        when(userRepo.existsUserByEmail(dto.getEmail())).thenReturn(true);

        service.sendUserViolationEmail(dto);

        verify(userRepo).existsUserByEmail(dto.getEmail());
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendSuccessRestorePasswordByEmailTest() {
        String email = "test@gmail.com";
        String lang = "en";
        String userName = "Helgi";
        boolean isUbs = false;
        service.sendSuccessRestorePasswordByEmail(email, lang, userName, isUbs);

        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendNotificationByEmail() {
        User user = User.builder().build();
        NotificationDto dto = NotificationDto.builder().title("title").body("body").build();
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        service.sendNotificationByEmail(dto, "test@gmail.com");
        verify(javaMailSender).createMimeMessage();
    }

    @Test
    void sendNotificationByEmailNotFoundException() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        NotificationDto dto = NotificationDto.builder().title("title").body("body").build();
        assertThrows(NotFoundException.class, () -> service.sendNotificationByEmail(dto, "test@gmail.com"));
    }
}
