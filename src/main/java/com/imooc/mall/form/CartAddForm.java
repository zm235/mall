package com.imooc.mall.form;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Add product into shopping cart
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;

}
