package com.yf.service;

import com.yf.po.Blog;
import com.yf.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

// 自定义接口
public interface BlogService {
    /**
     * 根据id获取Blog对象,Blog对象含有对应id的blog数据库中的一行数据
     * Params:
     * id – 传入的id
     * Returns:
     * 返回对应id的Blog对象
     * */
    Blog getBlog(Long id);  // 定义的接口方法getBlog(Long id);

    /**
     * 根据id获取Blog对象中的content,将content转换为HTML格式
     * Params:
     * id – 传入的id
     * Returns:
     * 返回转换后的Blog对象
     * */

    Blog getContentAndConvert(Long id);

    /**
     * 根据pageable获取所有的Blog,并且按照设定的分页样式筛选出文章状态为发表的Blog分页对象
     * Params:
     * id – 传入的id
     * Returns:
     * 返回筛选后的Blog分页对象
     * */

    Page<Blog> listBlog(Pageable pageable);

    /**
     * 根据标签的id与设定好的分页样式,获取指定标签下的Blog对象,并且筛选出文章状态为表发的Blog对象
     * Params:
     * id – 传入的id
     * Returns:
     * 返回筛选后的Blog分页对象=>用于标签页中显示每一个标签下的文章
     * */

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable, String query);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    int countPublishBlog();

    void deleteBlog(Long id);
}
