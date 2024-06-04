package greencity.security.dto.ownsecurity;

import greencity.annotations.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdatePasswordDto {

    @NotBlank
    @PasswordValidation
    private String currentPassword;

    @NotBlank
    @PasswordValidation
    private String newPassword;

    @NotBlank
    @PasswordValidation
    private String confirmPassword;
}
