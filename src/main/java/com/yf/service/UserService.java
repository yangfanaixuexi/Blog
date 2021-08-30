package com.yf.service;

import com.yf.po.User;

public interface UserService {
    // 检查用户名和密码
    User checkUser(String username, String password);
}
