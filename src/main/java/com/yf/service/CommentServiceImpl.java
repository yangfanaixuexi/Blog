package com.yf.service;

import com.yf.dao.CommentRepository;
import com.yf.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;


    // get collections of all child nodes
    private List<Comment> tempReplies = new ArrayList<>();

    @Transactional
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        tempReplies = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "creatTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Override
    public List<Comment> findAllByBlogId(Long blogId) {
        return commentRepository.findAllByBlogId(blogId);
    }


    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1)
        {
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        }
        else
        {
            comment.setParentComment(null);
        }
        comment.setCreatTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * iterate through each parent comment
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        // combine all child comments to parent comment group
        combineChildren(commentsView);
        return commentsView; // 这边很重要！！！传入数组是不改变的,改变的只有tempRelies
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replies = comment.getReplyComments();
            for(Comment reply : replies) {
                // find and save child comments in replies
                recursively(reply);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    /**
     * 递归迭代,剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment);    //tempReplies is a collection of all parent nodes
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                recursively(reply);
            }
        }
    }

    @Override
    public void deleteByBlogId(Long blogId) {
        commentRepository.deleteByBlogId(blogId);
    }
}
