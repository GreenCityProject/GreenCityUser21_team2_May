package greencity.mapping;

import greencity.dto.user.FriendDto;
import greencity.entity.User;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class FriendDtoMapper extends AbstractConverter<User, FriendDto> {

    @Override
    protected FriendDto convert(User user) {
        return FriendDto.builder()
                .id(user.getId())
                .profilePicture(user.getProfilePicturePath())
                .name(user.getName())
                .userCredo(user.getUserCredo())
                .city(user.getCity())
                .mutualFriends(3)
                .rating(user.getRating())
                .build();
    }
}
