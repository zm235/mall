package com.imooc.mall.controller;

import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;


@RestController
@RequestMapping("")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("Form submitted for register is not right: {} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);

        // dto
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  BindingResult bindingResult,
                                  HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        // Session setting
        httpSession.setAttribute(CURRENT_USER, userResponseVo.getData());
        log.info("/login sessionId={}", httpSession.getId());

        return userResponseVo;

    }

    // Save session in the memory (easy to lose: like restart the server or kill the process)
    // TODO: Improvement: save the session into the redis (token(sessionID) + redis)
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession httpSession) {
        log.info("/user sessionId={}", httpSession.getId());
        User user = (User) httpSession.getAttribute(CURRENT_USER);
        return ResponseVo.success(user);
    }


    // TODO: Check whether login or not ---> intercept
    @PostMapping("/user/logout")
    /**
     * {@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}
     */
    public ResponseVo<User> logout(HttpSession httpSession) {
        log.info("/user/logout sessionId={}", httpSession.getId());
        httpSession.removeAttribute(CURRENT_USER);
        return ResponseVo.success();
    }


}