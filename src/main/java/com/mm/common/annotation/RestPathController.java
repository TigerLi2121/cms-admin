package com.mm.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * RestPathController 替换@RequestMapping和@RestController注解
 *
 * @author lwl
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface RestPathController {

    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};

}
