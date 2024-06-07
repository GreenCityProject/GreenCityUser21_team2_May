package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;


import org.springframework.data.domain.Pageable;

public interface FriendService {
    PageableDto<UserFriendDto> getAllFriendsOfUser(long userId, Pageable pageable);

    void deleteFriendOfUser(Long id, Long friendId);
}
