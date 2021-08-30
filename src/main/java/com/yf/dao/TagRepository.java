package com.yf.dao;

import com.yf.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 与Tag相关的进行数据库的增删改查,M层(内部已经集成了一些API)
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByName(String name);

    void deleteTagById(Long id);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
