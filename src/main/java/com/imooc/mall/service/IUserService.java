package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;

public interface IUserService {
    /**
     * Register
     */
    ResponseVo<User> register(User user);

    /**
     * Login
     */
    ResponseVo<User> login(String username, String password);
    
}
