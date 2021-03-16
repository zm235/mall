package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;

public interface IUserService {
    /**
     * Register
     */
    ResponseVo register(User user);

    /**
     * Login
     */

}
