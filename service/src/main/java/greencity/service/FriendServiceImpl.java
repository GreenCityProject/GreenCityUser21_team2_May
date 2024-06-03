package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.user.FriendDto;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService{
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public PageableDto<FriendDto> getAllFriendsOfUser(long userId, Pageable pageable) {
        validateUserExistence(userId);

        Page<User> friends =userRepo.getAllFriendsOfUserIdPage(userId, pageable);

        List<FriendDto> friendList =
                friends.stream().map(friend -> modelMapper.map(friend, FriendDto.class))
                        .toList();

        return new PageableDto<>(
                friendList,
                friends.getTotalElements(),
                friends.getPageable().getPageNumber(),
                friends.getTotalPages());
    }

    @Override
    public void deleteFriendOfUser(Long userId, Long friendId) {
        validateUserExistence(userId);
        validateUserExistence(friendId);
        validateFriendsExistence(userId,friendId);
        userRepo.deleteUserFriendById(userId, friendId);
    }

    private void validateFriendsExistence(Long userId, Long friendId) {
        if(!userRepo.isFriend(userId,friendId)){
            throw new NotFoundException(ErrorMessage.USER_FRIEND_NOT_FOUND +  ": " + userId + " and " + friendId);
        }
    }


    private void validateUserExistence(long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
    }
}
