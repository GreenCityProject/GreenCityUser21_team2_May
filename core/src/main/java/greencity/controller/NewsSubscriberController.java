package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.service.EmailService;
import greencity.service.NewsSubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriber")
@AllArgsConstructor
public class NewsSubscriberController {
    @Autowired
    private final NewsSubscriberService subscriberService;

    /**
     * Method for subscription on interesting news for unregistered user via email.
     *
     * @param newsSubscriberRequestDto - object to send emails
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Save subscriber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@Valid @RequestParam NewsSubscriberRequestDto newsSubscriberRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(subscriberService.subscribe(newsSubscriberRequestDto));
    }

    /**
     * Method to get all subscribers.
     *
     * @return list of {@link NewsSubscriberResponseDto}
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get all emails for sending news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED)
    })
    @GetMapping
    public ResponseEntity<List<NewsSubscriberResponseDto>> getAllSubscribers() {
        return ResponseEntity.status(HttpStatus.OK).body(subscriberService.getAll());
    }
}
