package com.yf.dao;

import com.yf.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 与Comment相关的进行数据库的增删改查,M层(内部已经集成了一些API)
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    List<Comment> findAllByBlogId(Long blogId);

    void deleteByBlogId(Long blogId);
}
