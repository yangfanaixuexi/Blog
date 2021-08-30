package com.yf.service;

import com.yf.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    List<Comment> findAllByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    void deleteByBlogId(Long blogId);
}
