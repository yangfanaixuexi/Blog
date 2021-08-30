package com.yf.dao;

import com.yf.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 与User相关的进行数据库的增删改查,M层(内部已经集成了一些API)
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
