package com.imooc.mall.form;

import lombok.Data;

@Data
public class CartUpdateForm {
    // Both fields are not necessary.

    private Integer quantity;

    private Boolean selected;

}
