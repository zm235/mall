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

    PRODUCT_OFF_SALE_OR_DELETE(12, "Product is off sale or deleted"),

    PRODUCT_NOT_EXIST(13, "Product doesn't exist"),

    PRODUCT_STOCK_ERROR(14, "Inventory is not enough"),
    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
