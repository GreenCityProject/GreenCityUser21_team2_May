package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.friends.UserFriendDto;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserFriendDtoMapperTest {

    @InjectMocks
    UserFriendDtoMapper mapper;

    @Test
    void convert() {
        User user = ModelUtils.getUser();
        UserFriendDto expected = UserFriendDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        UserFriendDto actual = mapper.convert(user);

        assertEquals(expected, actual);

    }
}