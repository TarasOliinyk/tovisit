package com.lits.tovisitapp.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = NotBlankOrNull.Validator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankOrNull {
	String message() default "Cannot be blank or empty, null allowed";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<NotBlankOrNull, String> {
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return value == null || (!value.isBlank());
		}
	}
}
