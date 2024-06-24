package greencity.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TagUserDto {
    @NotNull
    private Long id;

    private String name;

    private String profilePicturePath;

    private String nickname;
}
