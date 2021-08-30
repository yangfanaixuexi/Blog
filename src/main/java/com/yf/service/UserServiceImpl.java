package com.yf.service;

import com.yf.dao.UserRepository;
import com.yf.po.User;
import com.yf.util.MD5Utils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
//    @Autowired
//    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
