package greencity.validator;

import greencity.annotations.NicknameValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNickname implements ConstraintValidator<NicknameValidation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) {
            return false;
        }

        if (s.length() < 4 || s.length() > 30) {
            return false;
        }

        return s.matches("\\w+");
    }
}
