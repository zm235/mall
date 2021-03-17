package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    ERROR(-1, "server error"),

    SUCCESS(0,"SUCCESS"),

    PASSWORD_ERROR(1, "Password Error"),

    USERNAME_EXIST(2, "Username Exists"),

    PARAM_ERROR(3, "Parameter Wrong"),

    EMAIL_EXIST(4, "Email Exists"),

    NEED_LOGIN(10, "User has not been logged in, cannot retrieve the user data"),

    USERNAME_OR_PASSWORD_ERROR(11, "Username or Password error"),

    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
