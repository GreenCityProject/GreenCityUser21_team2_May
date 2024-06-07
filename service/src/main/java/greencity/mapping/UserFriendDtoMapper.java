package greencity.mapping;

import greencity.dto.friends.UserFriendDto;
import greencity.entity.User;
import org.modelmapper.AbstractConverter;

public class UserFriendDtoMapper extends AbstractConverter<User, UserFriendDto> {

    @Override
    protected UserFriendDto convert(User user){
        return UserFriendDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }
}
