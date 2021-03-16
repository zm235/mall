package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserForm {
//    @NotBlank Userd for String -> can't be emptry or blank
//    @NotNull whether it is a null pointer
//    @NotEmpty Used for set map

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
