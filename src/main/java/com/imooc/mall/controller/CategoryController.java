package com.imooc.mall.controller;

import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("")
@Slf4j
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;


    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> categoriesInfo(HttpSession httpSession) {
        log.info("/categories sessionId={}", httpSession.getId());
        return categoryService.selectAll();
    }
}
