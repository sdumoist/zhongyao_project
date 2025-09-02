package com.xpxp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xpxp.Constant.ErrorConstant;
import com.xpxp.Exception.BaseException;
import com.xpxp.mapper.UserMapper;
import com.xpxp.pojo.dto.RegisterDTO;
import com.xpxp.pojo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDTO rejisterDTO) {
        // 1. 校验用户名是否已存在
        if (userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getUsername, rejisterDTO.getUsername()))) {
            throw new BaseException(ErrorConstant.USERNAME_EXISTS);
        }

        // 2. 密码加密并保存用户
        User user = new User();
        user.setUsername(rejisterDTO.getUsername());
        user.setPassword(passwordEncoder.encode(rejisterDTO.getPassword()));
        user.setRole("USER"); // 默认角色
        userMapper.insert(user);
    }

    public void resetPassword(RegisterDTO rejisterDTO) {
        // 1. 校验用户名是否已存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, rejisterDTO.getUsername()));
        if (user == null) {
            throw new BaseException(ErrorConstant.USER_NOT_FOUND);
        }

        //2. 重设密码并保存
        user.setPassword(passwordEncoder.encode(rejisterDTO.getPassword()));
        userMapper.updateById(user);
    }

}