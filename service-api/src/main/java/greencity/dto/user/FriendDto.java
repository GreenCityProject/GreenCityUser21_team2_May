package greencity.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class FriendDto {
    private Long id;
    private String profilePicture;
    private String name;
    private Double rating;
    private String userCredo;
    private String city;
    private Integer mutualFriends;
    private boolean isOnline;
}
