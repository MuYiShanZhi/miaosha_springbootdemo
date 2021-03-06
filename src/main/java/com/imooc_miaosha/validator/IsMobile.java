package com.imooc_miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {

    boolean required() default true;//运行在特殊时刻为空。如不传参数

    String message() default "手机号码格式错误";  //校验不通过时提示信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
