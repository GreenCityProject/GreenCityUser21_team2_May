package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.filter.FilterUserDto;
import greencity.dto.shoppinglist.CustomShoppingListItemResponseDto;
import greencity.dto.ubs.UbsTableCreationDto;
import greencity.dto.user.*;
import greencity.enums.EmailNotification;
import greencity.enums.Role;
import greencity.enums.UserStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

/**
 * Provides the interface to manage {UserVO} entity.
 *
 * @author Nazar Stasyuk and Rostyslav && Yurii Koval
 * @version 1.0
 */
public interface UserService {
    /**
     * Find all {@link User}'s with {@link EmailNotification} type.
     *
     * @param emailNotification - type of {@link EmailNotification}
     * @return list of {@link User}'s
     */
    List<UserVO> findAllByEmailNotification(EmailNotification emailNotification);

    /**
     * Delete from the database users that have status 'DEACTIVATED' and last
     * visited the site 2 years ago.
     *
     * @return number of deleted rows.
     */
    int scheduleDeleteDeactivatedUsers();

    /**
     * Find and return all cities for all users.
     *
     * @return {@link List} of {@link String} of cities
     **/
    List<String> findAllUsersCities();

    /**
     * Method that allow you to save new {@link UserVO}.
     *
     * @param user a value of {@link UserVO}
     * @author Yurii Koval
     */
    UserVO save(UserVO user);

    /**
     * Method that allow you to find {@link UserVO} by ID.
     *
     * @param id a value of {@link Long}
     * @return {@link UserVO}
     */
    UserVO findById(Long id);

    /**
     * Method that allow you to delete {@link UserVO} by ID.
     *
     * @param id a value of {@link Long}
     */
    void deleteById(Long id);

    /**
     * Method that allow you to find {@link UserVO} by email.
     *
     * @param email a value of {@link String}
     * @return {@link UserVO} with this email.
     */
    UserVO findByEmail(String email);

    /**
     * Method that allow you to find not 'DEACTIVATED' {@link UserVO} by email.
     *
     * @param email - {@link UserVO}'s email
     * @return {@link Optional} of found {@link UserVO}.
     * @author Vasyl Zhovnir
     */
    Optional<UserVO> findNotDeactivatedByEmail(String email);

    /**
     * Find UserVO's id by UserVO email.
     *
     * @param email - {@link UserVO} email
     * @return {@link UserVO} id
     * @author Zakhar Skaletskyi
     */
    Long findIdByEmail(String email);

    /**
     * Find UserVO's uuid by UserVO email.
     *
     * @param email - {@link UserVO} email
     * @return {@link UserVO} uuid
     */
    String findUuIdByEmail(String email);

    /**
     * Update {@code ROLE} of user.
     *
     * @param id   {@link UserVO} id.
     * @param role {@link Role} for user.
     * @return {@link UserRoleDto}
     * @author Rostyslav Khasanov
     */
    UserRoleDto updateRole(Long id, Role role, String email);

    /**
     * Update status of user.
     *
     * @param id         {@link UserVO} id.
     * @param userStatus {@link UserStatus} for user.
     * @return {@link UserStatusDto}
     * @author Rostyslav Khasanov
     */
    UserStatusDto updateStatus(Long id, UserStatus userStatus, String email);

    /**
     * Find {@link UserVO}-s by page .
     *
     * @param pageable a value with pageable configuration.
     * @return a dto of {@link PageableDto}.
     * @author Rostyslav Khasanov
     */
    PageableDto<UserForListDto> findByPage(Pageable pageable);

    /**
     * Find {@link UserVO} for management by page .
     *
     * @param pageable a value with pageable configuration.
     * @return a dto of {@link PageableAdvancedDto}.
     * @author Vasyl Zhovnir
     */
    PageableAdvancedDto<UserManagementDto> findUserForManagementByPage(Pageable pageable);

    /**
     * Method that allows you to update {@link UserVO} by dto.
     *
     * @param dto - dto {@link UserManagementDto} with updated fields for updating
     *            {@link UserVO}.
     * @author Vasyl Zhovnir
     */
    void updateUser(Long userId, UserManagementUpdateDto dto);

    /**
     * Get all exists roles.
     *
     * @return {@link RoleDto}.
     * @author Rostyslav Khasanov
     */
    RoleDto getRoles();

    /**
     * Get list of available {@link EmailNotification} statuses for {@link UserVO}.
     *
     * @return available {@link EmailNotification} statuses.
     */
    List<EmailNotification> getEmailNotificationsStatuses();

    /**
     * Update last visit of user.
     *
     * @return {@link UserVO}.
     */
    UserVO updateLastVisit(UserVO user);

    /**
     * Find users by filter.
     *
     * @param filterUserDto contains objects whose values determine the filter
     *                      parameters of the returned list.
     * @param pageable      pageable configuration.
     * @return {@link PageableDto}.
     * @author Rostyslav Khasanov.
     */
    PageableDto<UserForListDto> getUsersByFilter(FilterUserDto filterUserDto, Pageable pageable);

    /**
     * Get {@link UserVO} dto by principal (email).
     *
     * @param email - email of user.
     * @return {@link UserUpdateDto}.
     * @author Nazar Stasyuk
     */
    UserUpdateDto getUserUpdateDtoByEmail(String email);

    /**
     * Update {@link UserVO}.
     *
     * @param dto   {@link UserUpdateDto} - dto with new {@link UserVO} params.
     * @param email {@link String} - email of user that need to update.
     * @return {@link UserVO}.
     * @author Nazar Stasyuk
     */
    UserUpdateDto update(UserUpdateDto dto, String email);

    /**
     * Updates refresh token for a given user.
     *
     * @param refreshTokenKey - new refresh token key
     * @param id              - user's id
     * @return - number of updated rows
     */
    int updateUserRefreshToken(String refreshTokenKey, Long id);

    /**
     * Method returns list of available (not ACTIVE) customShoppingListItem for
     * user.
     *
     * @param userId id of the {@link UserVO} current user.
     * @return List of {@link CustomShoppingListItemResponseDto}
     * @author Bogdan Kuzenko
     */
    List<CustomShoppingListItemResponseDto> getAvailableCustomShoppingListItems(Long userId, Long habitID);

    /**
     * Counts all users by user {@link UserStatus} ACTIVATED.
     *
     * @return amount of users with {@link UserStatus} ACTIVATED.
     * @author Shevtsiv Rostyslav
     */
    long getActivatedUsersAmount();

    /**
     * Get profile picture path {@link String}.
     *
     * @return profile picture path {@link String}
     */
    String getProfilePicturePathByUserId(Long id);

    /**
     * Update user profile picture {@link UserVO}.
     *
     * @param image  {@link MultipartFile}
     * @param email  {@link String} - email of user that need to update.
     * @param base64 {@link String} - picture in base 64 format.
     * @return {@link UserVO}.
     * @author Marian Datsko
     */
    UserVO updateUserProfilePicture(MultipartFile image, String email,
        String base64);

    /**
     * Delete user profile picture {@link UserVO}.
     *
     * @param email {@link String} - email of user that need to update.
     */
    void deleteUserProfilePicture(String email);

    /**
     * Save user profile information {@link UserVO}.
     *
     * @author Marian Datsko
     */
    String saveUserProfile(UserProfileDtoRequest userProfileDtoRequest, String name);

    /**
     * Updates last activity time for a given user.
     *
     * @param userId               - {@link UserVO}'s id
     * @param userLastActivityTime - new {@link UserVO}'s last activity time
     * @author Yurii Zhurakovskyi
     */
    void updateUserLastActivityTime(Long userId, LocalDateTime userLastActivityTime);

    /**
     * The method checks by id if a {@link UserVO} is online.
     *
     * @param userId - {@link UserVO}'s id
     * @author Yurii Zhurakovskyi
     */
    boolean checkIfTheUserIsOnline(Long userId);

    /**
     * Method return user profile information {@link UserVO}.
     *
     * @param userId - {@link UserVO}'s id
     * @author Marian Datsko
     */
    UserProfileDtoResponse getUserProfileInformation(Long userId);

    /**
     * Method return user profile statistics {@link UserVO}.
     *
     * @param userId - {@link UserVO}'s id
     * @author Marian Datsko
     */
    UserProfileStatisticsDto getUserProfileStatistics(Long userId);

    /**
     * Method deactivates all the {@link UserVO} by list of IDs.
     *
     * @param listId {@link List} of {@link UserVO}s` ids to be deactivated
     * @return {@link List} of {@link UserVO}s` ids
     * @author Vasyl Zhovnir
     */
    List<Long> deactivateAllUsers(List<Long> listId);

    /**
     * change {@link UserVO}'s status to ACTIVATED.
     *
     * @param id {@link UserVO}'s id
     * @author Vasyl Zhovnir
     */
    UserActivationDto setActivatedStatus(Long id);

    /**
     * Method that allow you to find {@link UserVO} by ID and token.
     *
     * @param userId - {@link UserVO}'s id
     * @param token  - {@link UserVO}'s token
     * @return {@link Optional} of {@link UserVO}
     */
    Optional<UserVO> findByIdAndToken(Long userId, String token);

    /**
     * Method for getting UserVO by search query.
     *
     * @param paging {@link Pageable}.
     * @param query  query to search,
     * @return {@link PageableAdvancedDto} of {@link UserManagementDto} instances.
     */
    PageableAdvancedDto<UserManagementDto> searchBy(Pageable paging, String query);

    /**
     * Method for getting all Users.
     *
     * @return {@link List} of {@link UserVO} instances.
     */
    List<UserVO> findAll();

    /**
     * {@inheritDoc}
     */
    PageableAdvancedDto<UserManagementVO> search(Pageable pageable, UserManagementViewDto userManagementViewDto);

    /**
     * Creates and returns uuid of current user.
     *
     * @param currentUser {@link UserVO} - current user.
     * @return {@link UbsTableCreationDto} - uuid of current user.
     */
    UbsTableCreationDto createUbsRecord(UserVO currentUser);

    /**
     * change {@link UserVO}'s status to DEACTIVATED.
     *
     * @param id          {@link UserVO}'s id
     * @param userReasons {@link List} of {@link String}.
     * @author Vasyl Zhovnir
     */
    UserDeactivationReasonDto deactivateUser(Long id, List<String> userReasons);

    /**
     * Method for getting a {@link List} of {@link String} - reasons for
     * deactivation of the current user.
     *
     * @param id        {@link Long} - user's id.
     * @param adminLang {@link String} - current administrator language.
     * @return {@link List} of {@link String}.
     * @author Vlad Pikhotskyi
     */
    List<String> getDeactivationReason(Long id, String adminLang);

    /**
     * Method that update user language column.
     *
     * @param userId     {@link Long} -current user's id.
     * @param languageId {@link Long} - language id.
     */
    void updateUserLanguage(Long userId, Long languageId);

    /**
     * Method find user with admin authority.
     *
     * @author Ihor Volianskyi
     */
    UserVO findAdminById(Long id);

    List<TagUserDto> searchByNickname(String nickname);

    TagUserDto getUserByNickname(String nickname);

    TagUserDto updateUserNickname(String nickname, String principal);
}
