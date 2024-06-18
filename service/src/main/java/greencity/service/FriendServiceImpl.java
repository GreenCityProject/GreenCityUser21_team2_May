package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.user.FriendDto;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Value("${greencity.time.after.last.activity}")
    private long timeAfterLastActivity;

    @Override
    public PageableDto<FriendDto> getAllFriendsOfUser(long userId, Pageable pageable) {
        validateUserExistence(userId);

        Page<User> friends = userRepo.getAllFriendsOfUserIdPage(userId, pageable);

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
        validateFriendsExistence(userId, friendId);
        userRepo.deleteUserFriendById(userId, friendId);
    }

    private void validateFriendsExistence(Long userId, Long friendId) {
        if (!userRepo.isFriend(userId, friendId)) {
            throw new NotFoundException(ErrorMessage.USER_FRIEND_NOT_FOUND + ": " + userId + " and " + friendId);
        }
    }

    private void validateUserExistence(long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<FriendDto> getAllFriendsByUserId(Pageable pageable, Long userId) {
        userRepo.findById(userId)
            .orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));

        Page<User> usersFriendsList;
        String pageableWithSort = pageable.getSort().toString();

        usersFriendsList = switch (pageableWithSort) {
            case "city" -> userRepo.findAllFriendsByUserIdAndCity(userId, pageable);
            case "habit" -> userRepo.findAllFriendsByUserIdAndHabitsAssigned(userId, pageable);
            case "rating" -> userRepo.findAllFriendsByUserIdAndRating(userId, pageable);
            default -> userRepo.findAllFriendsByUserId(userId, pageable);
        };

        return getFriendDtoPageableDto(userId, usersFriendsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<FriendDto> getAllFriendsByUserIdAndCity(Pageable pageable, Long userId) {
        userRepo.findById(userId)
            .orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));

        Page<User> usersFriendsList = userRepo.findAllFriendsByUserIdAndCity(userId, pageable);

        return getFriendDtoPageableDto(userId, usersFriendsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<FriendDto> getAllFriendsByUserIdAndRating(Pageable pageable, Long userId) {
        userRepo.findById(userId)
            .orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));

        Page<User> usersFriendsList = userRepo.findAllFriendsByUserIdAndRating(userId, pageable);

        return getFriendDtoPageableDto(userId, usersFriendsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<FriendDto> getAllFriendsByUserIdAndHabitsAssigned(Pageable pageable, Long userId) {
        userRepo.findById(userId)
            .orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));

        Page<User> usersFriendsList = userRepo.findAllFriendsByUserIdAndHabitsAssigned(userId, pageable);

        return getFriendDtoPageableDto(userId, usersFriendsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTotalAmountOfFriendsByUserId(Long userId) {
        userRepo.findById(userId)
            .orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));

        return userRepo.totalAmountOfFriendsByUserId(userId);
    }

    private PageableDto<FriendDto> getFriendDtoPageableDto(Long userId, Page<User> usersFriendsList) {
        List<FriendDto> userForListDtos = usersFriendsList.stream()
            .map(user -> {
                FriendDto friendDto = modelMapper.map(user, FriendDto.class);
                Integer mutualFriendsCount = userRepo.countOfMutualFriends(userId, friendDto.getId());
                friendDto.setMutualFriends(mutualFriendsCount);
                friendDto.setOnline(this.checkIfTheUserIsOnline(friendDto.getId()));
                return friendDto;
            })
            .toList();

        return new PageableDto<>(
            userForListDtos,
            usersFriendsList.getTotalElements(),
            usersFriendsList.getPageable().getPageNumber(),
            usersFriendsList.getTotalPages());
    }

    private boolean checkIfTheUserIsOnline(Long userId) {
        if (userRepo.findById(userId).isEmpty()) {
            throw new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
        Optional<Timestamp> lastActivityTime = userRepo.findLastActivityTimeById(userId);
        if (lastActivityTime.isPresent()) {
            LocalDateTime userLastActivityTime = lastActivityTime.get().toLocalDateTime();
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime lastActivityTimeZDT = ZonedDateTime.of(userLastActivityTime, ZoneId.systemDefault());
            long result = now.toInstant().toEpochMilli() - lastActivityTimeZDT.toInstant().toEpochMilli();
            return result <= timeAfterLastActivity;
        }
        return false;
    }
}
