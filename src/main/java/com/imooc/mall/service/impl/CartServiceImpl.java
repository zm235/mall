package com.imooc.mall.service.impl;

import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<CartVo> add(CartAddForm cartAddForm) {
        productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        // Check whether product exists

        // Check whether product is on sale status

        // Check whether product inventory is enough


        return null;
    }
}
