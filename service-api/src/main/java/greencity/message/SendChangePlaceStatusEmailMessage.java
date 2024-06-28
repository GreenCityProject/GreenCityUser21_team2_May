package greencity.message;

import greencity.constant.ValidationConstants;
import java.io.Serializable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class SendChangePlaceStatusEmailMessage implements Serializable {
    private String authorFirstName;

    private String placeName;

    private String placeStatus;

    @Email(message = ValidationConstants.INVALID_EMAIL)
    private String authorEmail;
}