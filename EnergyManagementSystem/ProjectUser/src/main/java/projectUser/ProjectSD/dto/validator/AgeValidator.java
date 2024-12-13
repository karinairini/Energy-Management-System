package projectUser.ProjectSD.dto.validator;

import org.springframework.stereotype.Component;
import projectUser.ProjectSD.dto.validator.annotation.AgeLimit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator class that checks if a given age exceeds a specified limit.
 * This class implements ConstraintValidator interface to provide custom
 * validation logic for the AgeLimit annotation.
 */
@Component
public class AgeValidator implements ConstraintValidator<AgeLimit, Integer> {
    private Integer ageLimit;

    /**
     * Initializes the validator with the age limit specified in the AgeLimit annotation.
     *
     * @param constraintAnnotation the annotation instance containing the age limit
     */
    @Override
    public void initialize(AgeLimit constraintAnnotation) {
        this.ageLimit = constraintAnnotation.limit();
    }

    /**
     * Validates whether the given input age is greater than the defined age limit.
     *
     * @param inputAge                   the age to be validated
     * @param constraintValidatorContext context for validation operations
     * @return true if the input age exceeds the age limit; false otherwise
     */
    @Override
    public boolean isValid(Integer inputAge, ConstraintValidatorContext constraintValidatorContext) {
        return inputAge > ageLimit;
    }
}
