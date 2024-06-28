package greencity.dto.newssubscriber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

import static greencity.constant.AppConstant.VALIDATION_EMAIL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsSubscriberRequestDto implements Serializable {
    @NotBlank
    @Email(regexp = VALIDATION_EMAIL)
    private String email;
}