package greencity.controller;


import greencity.annotations.ApiPageable;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.user.TagUserDto;
import greencity.dto.user.UserManagementDto;
import greencity.dto.user.UserUpdateDto;
import greencity.dto.user.UserVO;
import greencity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tagging")
@AllArgsConstructor
public class TaggingUserController {
    private final UserService userService;

    /**
     * Method that allow you to find all users {@link List<TagUserDto>} by match in query.
     *
     * @return {@link List<TagUserDto>}.
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Search User by nickname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
    })
    @GetMapping("/searchByNickname")
    @ApiPageable
    public ResponseEntity<List<TagUserDto>> searchByNickname(
            @RequestParam(name = "nickname", required = false) String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchByNickname(nickname));
    }


    /**
     * Method that allow you to find {@link TagUserDto} by nickname.
     *
     * @return {@link TagUserDto}.
     * @author Dmytro Fedotov
     */
    @Operation(summary = "Get User by nickname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/getByNickname")
    public ResponseEntity<TagUserDto> getByNickname(
            @RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByNickname(nickname));
    }
}
