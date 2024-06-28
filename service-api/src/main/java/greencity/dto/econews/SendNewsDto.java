package greencity.dto.econews;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SendNewsDto implements Serializable {

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotEmpty
    private String author;

    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime creationDate;

    @NotEmpty
    private String imagePath;

    private String source;
}