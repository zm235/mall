package com.imooc.mall.service;

import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.service.impl.CategoryServiceImpl;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ICategoryServiceTest {

    @Autowired
    ICategoryService categoryService = new CategoryServiceImpl();

    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> listResponseVo = categoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), listResponseVo.getStatus());
    }
}