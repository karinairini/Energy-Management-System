package projectDevice.ProjectSD.dto.validator;

import org.springframework.stereotype.Component;
import projectDevice.ProjectSD.dto.validator.annotation.ConsumptionLimit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator class that checks if a given consumption exceeds a specified limit.
 * This class implements ConstraintValidator interface to provide custom
 * validation logic for the ConsumptionLimit annotation.
 */
@Component
public class ConsumptionValidator implements ConstraintValidator<ConsumptionLimit, Double> {
    private Double consumptionLimit;

    /**
     * Initializes the validator with the consumption limit specified in the ConsumptionLimit annotation.
     *
     * @param constraintAnnotation the annotation instance containing the consumption limit.
     */
    @Override
    public void initialize(ConsumptionLimit constraintAnnotation) {
        this.consumptionLimit = constraintAnnotation.limit();
    }

    /**
     * Validates whether the given input consumption is greater than the defined consumption limit.
     *
     * @param inputConsumption           the consumption to be validated.
     * @param constraintValidatorContext context for validation operations.
     * @return true if the input consumption exceeds the consumption limit; false otherwise.
     */
    @Override
    public boolean isValid(Double inputConsumption, ConstraintValidatorContext constraintValidatorContext) {
        return inputConsumption > consumptionLimit;
    }
}
