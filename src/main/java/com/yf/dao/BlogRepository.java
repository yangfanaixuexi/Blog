package com.yf.dao;

import com.yf.po.Blog;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 与Blog相关的进行数据库的增删改查,M层(内部已经集成了一些API),JpaRepository与JpaSpecificationExecutor中进行数据库申明
public interface BlogRepository extends JpaRepository<Blog, Long> , JpaSpecificationExecutor<Blog> {
    Blog findByTitle(String title);

    void deleteById(@NotNull Long id);

    @Query("select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

    // 这边pageable会默认不是输入query, 从query里面拿1个, 这里的1匹配query而且代表只有1个变量
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(Pageable pageable, String query);

    @Transactional
    @Modifying
    @Query("update Blog  b set b.views= b.views+1 where b.id = ?1")
    void updateViews(Long id);

    @Query("select function('date_format', b.updateTime, '%Y') as year from Blog b where b.published = true group by year order by function('date_format', b.updateTime, '%Y') desc")
    List<String> findGroupYear();

    @Query("SELECT b from Blog b where function('date_format', b.updateTime, '%Y') = ?1 and b.published = true")
    List<Blog> findByYear(String year);

    @Query("SELECT b from Blog b where b.published = true ")
    List<Blog> findPublishBlog();
}
