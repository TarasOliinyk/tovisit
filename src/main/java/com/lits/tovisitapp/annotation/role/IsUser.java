package com.lits.tovisitapp.annotation.role;

import com.lits.tovisitapp.data.UserRole;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(UserRole.Name.USER)
public @interface IsUser {
}
