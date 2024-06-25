package greencity.annotations;


import greencity.validator.ValidNickname;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidNickname.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NicknameValidation {
    String message() default "Invalid nickname: nickname must be 4-30 symbols, latin letters, may contain '_' ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
