package com.yf.dao;

import com.yf.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 与Type相关的进行数据库的增删改查,M层(内部已经集成了一些API)
public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findTypeByName(String name);

    void deleteTypeById(Long id);

    @Query("SELECT t from Type t")
    List<Type> findTop(Pageable pageable);
}
