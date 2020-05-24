package com.lits.tovisitapp.annotation;

import com.lits.tovisitapp.data.PermissionLevel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + PermissionLevel.Name.READ + "')")
public @interface HasReadPermission {
}
