package greencity.controller;


import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.user.FriendDto;
import greencity.dto.user.UserVO;
import greencity.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


@AllArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;


    @Operation(summary = "Get all friends of Current User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED)))
    })
    @GetMapping
    public ResponseEntity<PageableDto<FriendDto>> getAllFriendsOfUser(
            @Parameter(hidden = true) Pageable page,
            @Parameter(hidden = true) @CurrentUser UserVO userVO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(friendService.getAllFriendsOfUser( userVO.getId(), page));
    }



    @Operation(summary = "Delete friend for Current User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @DeleteMapping("/deleteFriend/{friendId}")
    public ResponseEntity<FriendDto> deleteFriendForUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @Parameter @PathVariable long friendId) {
        friendService.deleteFriendOfUser( userVO.getId(), friendId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }



}
