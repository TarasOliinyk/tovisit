package com.lits.tovisitapp.annotation.role;

import com.lits.tovisitapp.data.UserRole;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(UserRole.Name.ADMIN)
public @interface IsAdmin {
}
