package projectUser.ProjectSD.dto.validator.annotation;

import projectUser.ProjectSD.dto.validator.AgeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation to specify an age limit for validation.
 * This annotation can be applied to methods, fields, and parameters.
 * It uses the AgeValidator class to enforce the age limit.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AgeValidator.class})
public @interface AgeLimit {

    /**
     * Specifies the minimum age limit.
     *
     * @return the minimum age limit, default is 18
     */
    int limit() default 18;

    /**
     * The error message to be returned when validation fails.
     *
     * @return the error message, default is "Age does not meet the required limit for an adult user."
     */
    String message() default "Age does not meet the required limit for an adult user.";

    /**
     * Allows the specification of validation groups.
     *
     * @return the groups, default is an empty array
     */
    Class<?>[] groups() default {};

    /**
     * Carries metadata information for the annotation.
     *
     * @return the payload, default is an empty array
     */
    Class<? extends Payload>[] payload() default {};
}
