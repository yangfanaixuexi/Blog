package com.yf.service;

import com.yf.NotFoundException;
import com.yf.dao.BlogRepository;
import com.yf.po.Blog;
import com.yf.po.Comment;
import com.yf.po.Type;
import com.yf.util.MarkdownUtils;
import com.yf.util.MyBeanUtils;
import com.yf.vo.BlogQuery;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentService commentService;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getById(id);
    }

    @Transactional
    @Override
    public Blog getContentAndConvert(Long id) {
        Blog transBlog = new Blog();
        Blog blog = blogRepository.getById(id);
        if(blog == null)
        {
            throw new  NotFoundException("该博客不存在");
        }

        BeanUtils.copyProperties(blog, transBlog);  // 将blog对象拷贝给transBlog,避免对数据库进行操作
        String content = transBlog.getContent();
        transBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);

        return transBlog;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.<Boolean>get("published"), true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(@NotNull Root<Blog> root, @NotNull CriteriaQuery<?> cq, @NotNull CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 关联表查询
                Join<Object, Object> join = root.join("tags");
                predicates.add(cb.equal(join.get("id"), tagId));
                predicates.add(cb.equal(root.<Boolean>get("published"), true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null)
                {
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }

                if(blog.getTypeId() != null)        // 如果直接从Blog中的blog对象获取Type，Type是一个空指针,会报错
                {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                    predicates.add(cb.equal(root.<Boolean>get("published"), true));
                }

                if(blog.getRecommend() != null)
                {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.getRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(pageable, query);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> BlogMap = new HashMap<>();
        for (String year : years)
        {
            BlogMap.put(year, blogRepository.findByYear(year));
        }
        return BlogMap;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null || blog.getId() == 0)
        {
            blog.setCreatTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }
        else
        {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog findBlog = blogRepository.getById(id);
        if(findBlog == null)
        {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, findBlog, MyBeanUtils.getNullPropertyNames(blog));   // 过滤blog对象中属性值为空的属性
        findBlog.setUpdateTime(new Date());
        return blogRepository.save(findBlog);
    }

    @Override
    public int countPublishBlog() {
        return (blogRepository.findPublishBlog().size());
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        // 找到相应的文章
        Blog findBlog = blogRepository.getById(id);
        // 找到与文章相关的评论
        List<Comment> findComments = commentService.findAllByBlogId(id);
        if(findBlog.getComments() != null)
        {
            // 循环删除每一条评论
            for(Comment comment : findComments)
            {
                commentService.deleteByBlogId(comment.getBlog().getId());
            }
        }
        // 最后删除文章
        blogRepository.deleteById(id);
    }

}
