package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.exception.exceptions.NotFoundException;
import greencity.service.NewsSubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriber")
@AllArgsConstructor
public class NewsSubscriberController {

    private final NewsSubscriberService subscriberService;


    /**
     * Method for subscription on interesting news for unregistered user via email.
     *
     * @param newsSubscriberRequestDto - object to send emails
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Save subscriber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
    })
    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@Valid @RequestBody NewsSubscriberRequestDto newsSubscriberRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.subscribe(newsSubscriberRequestDto));
    }


    /**
     * Method for getting all subscribers.
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


    /**
     * Method for unsubscribing.
     *
     * @param email email of subscriber.
     * @param unsubscribeToken token of subscriber.
     */
    @Operation(summary = "Deleting an email form subscribe table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(schema = @Schema(implementation = NotFoundException.class)))
    })
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Long> unsubscribe(@RequestParam @Email String email, @RequestParam String unsubscribeToken){
        // todo: token transfering is safer in body?
        subscriberService.unsubscribe(email, unsubscribeToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
