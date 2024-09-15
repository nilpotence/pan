package fr.nilpo.pan.models.boulders;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidHoldsValidator implements ConstraintValidator<ValidHolds, String>{
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			ShowBoulderHelper.parseHolds(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
