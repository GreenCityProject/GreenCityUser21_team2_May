package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.user.FriendDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface FriendService {
    PageableDto<FriendDto> getAllFriendsOfUser(long userId, Pageable pageable);

    void deleteFriendOfUser(Long id, Long friendId);

    /**
     * Method that allow you to find {@link List} of {@link FriendDto} by user's ID.
     *
     * @param userId a value of {@link Long}
     * @return {@link List} of {@link FriendDto}
     */
    PageableDto<FriendDto> getAllFriendsByUserId(Pageable pageable, Long userId);

    /**
     * Method that allow you to find {@link List} of {@link FriendDto} by user's ID
     * and city.
     *
     * @param userId a value of {@link Long}
     * @return {@link List} of {@link FriendDto}
     */
    PageableDto<FriendDto> getAllFriendsByUserIdAndCity(Pageable pageable, Long userId);

    /**
     * Method that allow you to find {@link List} of {@link FriendDto} by user's ID
     * and sorted by rating.
     *
     * @param userId a value of {@link Long}
     * @return {@link List} of {@link FriendDto}
     */
    PageableDto<FriendDto> getAllFriendsByUserIdAndRating(Pageable pageable, Long userId);

    /**
     * Method that allow you to find {@link List} of {@link FriendDto} by user's ID
     * and sorted by habits assigned.
     *
     * @param userId a value of {@link Long}
     * @return {@link List} of {@link FriendDto}
     */
    PageableDto<FriendDto> getAllFriendsByUserIdAndHabitsAssigned(Pageable pageable, Long userId);

    /**
     * Method that allow you to get total amount of friends by user's ID.
     *
     * @param userId a value of {@link Long}
     * @return {@link Integer} total amount of friends
     * @author Maksym Petukhov
     */
    Integer getTotalAmountOfFriendsByUserId(Long userId);
}