package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductVoList {

    private Integer productId;

    /**
     * number of product in shopping cart
     */
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    /**
     * total price == price * quntity
     */
    private BigDecimal productTotalPrice;

    private Integer productStock;

    private Boolean productSelected;


    private Integer categoryId;

}
