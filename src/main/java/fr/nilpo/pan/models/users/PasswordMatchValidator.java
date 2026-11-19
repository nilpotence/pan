package fr.nilpo.pan.models.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AppUser>{
	@Override
	public boolean isValid(AppUser value, ConstraintValidatorContext context) {
		return value.getPassword().equals(value.getPasswordConfirmation());
	}
}
