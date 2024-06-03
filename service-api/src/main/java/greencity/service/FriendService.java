package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.user.FriendDto;
import org.springframework.data.domain.Pageable;

public interface FriendService {
    PageableDto<FriendDto> getAllFriendsOfUser(long userId, Pageable pageable);

    void deleteFriendOfUser(Long id, Long friendId);
}
