package projectDevice.ProjectSD.dto.validator.annotation;

import jakarta.validation.Payload;
import projectDevice.ProjectSD.dto.validator.ConsumptionValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * Annotation to specify a consumption limit for validation.
 * This annotation can be applied to methods, fields, and parameters.
 * It uses the ConsumptionValidator class to enforce the consumption limit.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ConsumptionValidator.class})
public @interface ConsumptionLimit {

    /**
     * Specifies the minimum consumption limit.
     *
     * @return the minimum consumption limit, default is 0.0
     */
    double limit() default 0.0;

    /**
     * The error message to be returned when validation fails.
     *
     * @return the error message, default is "The maximum hourly consumption must be greater than the default limit."
     */
    String message() default "The maximum hourly consumption must be greater than the {limit}.";

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
