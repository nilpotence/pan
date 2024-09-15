package fr.nilpo.pan.models.boulders;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ValidHoldsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHolds {
	String message() default "Vous devez choisir au moins une prise pour votre bloc !";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
