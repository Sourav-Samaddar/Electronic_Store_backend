package com.lcwd.electronic.store.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

	//error message
	String message() default "Image name is invalid !!";

	//represents group of error message
	Class<?>[] groups() default { };

	//additional information about payload
	Class<? extends Payload>[] payload() default { };
}
