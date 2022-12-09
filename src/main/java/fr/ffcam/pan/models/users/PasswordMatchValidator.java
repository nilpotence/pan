package fr.ffcam.pan.models.users;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AppUser>{
	@Override
	public boolean isValid(AppUser value, ConstraintValidatorContext context) {
		return value.getPassword().equals(value.getPasswordConfirmation());
	}
}
