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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nullable;


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


    @Operation(summary = "Add new Friend for Current logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })

    @PostMapping("/addFriend/{friendId}")
    public ResponseEntity<UserVO> addFriendForCurrentUser(
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @Parameter @PathVariable long friendId){

        friendService.addNewFriend(userVO.getId(), friendId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
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

    @GetMapping("/searchNewFriend/{searchName}/city={cityFilter}/mutualFriend={mutualFriend}")
    public ResponseEntity<PageableDto<FriendDto>> searchNewFriend(
            @Parameter(hidden = true) Pageable page,
            @Parameter(hidden = true) @CurrentUser UserVO userVO,
            @Parameter @PathVariable String searchName,
            @Parameter @PathVariable @Nullable Boolean cityFilter,
            @Parameter @PathVariable @Nullable Boolean mutualFriend
    ){

        String city = null;
        if ( Boolean.TRUE.equals(cityFilter)) city = userVO.getCity();

        PageableDto<FriendDto> friendsResult = friendService.searchNewFriend(userVO.getId(), searchName, city, mutualFriend, page);


        return ResponseEntity.status(HttpStatus.OK).body(friendsResult);
    }



    /**
     * Method that find all user's friends.
     *
     * @param userId - user id
     * @param pageable - pageable configuration
     * @return {@link PageableDto} of {@link FriendDto}
     */
    @Operation(summary = "Find all user's friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/{userId}/findAllFriends")
    public ResponseEntity<PageableDto<FriendDto>> findAllFriends(@PathVariable Long userId, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriendsByUserId(pageable, userId));
    }

    /**
     * Method that find all user's friends by user's city.
     *
     * @param userId - user id
     * @param pageable - pageable configuration
     * @return {@link PageableDto} of {@link FriendDto}
     */
    @Operation(summary = "Find all user's friends by user's city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/{userId}/findAllFriendsByCity")
    public ResponseEntity<PageableDto<FriendDto>> findAllFriendsByCity(@PathVariable Long userId, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriendsByUserIdAndCity(pageable, userId));
    }

    /**
     * Method that find all user's friends sorted by rating.
     *
     * @param userId - user id
     * @param pageable - pageable configuration
     * @return {@link PageableDto} of {@link FriendDto}
     */
    @Operation(summary = "Find all user's friends sorted by rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/{userId}/findAllFriendsByRating")
    public ResponseEntity<PageableDto<FriendDto>> findAllFriendsByRating(@PathVariable Long userId, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriendsByUserIdAndRating(pageable, userId));
    }

    /**
     * Method that find all user's friends by user's and his/her friends assigned habits.
     *
     * @param userId - user id
     * @param pageable - pageable configuration
     * @return {@link PageableDto} of {@link FriendDto}
     */
    @Operation(summary = "Find all user's friends by user's and his/her friends assigned habits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/{userId}/findAllFriendsByHabit")
    public ResponseEntity<PageableDto<FriendDto>> findAllFriendsByHabit(@PathVariable Long userId, @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllFriendsByUserIdAndHabitsAssigned(pageable, userId));
    }

    /**
     * Method that find all user's friends by user's and his/her friends assigned habits.
     *
     * @param userId - user id
     * @return {@link Integer} - total amount of friends
     */
    @Operation(summary = "Get total amount of friends by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.BAD_REQUEST),
    })
    @GetMapping("/{userId}/totalAmountOfFriends")
    public ResponseEntity<Integer> getTotalAmountOfFriends(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getTotalAmountOfFriendsByUserId(userId));
    }
}
