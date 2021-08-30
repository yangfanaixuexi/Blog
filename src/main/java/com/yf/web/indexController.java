package com.yf.web;

import com.yf.po.Tag;
import com.yf.service.BlogService;
import com.yf.service.TagService;
import com.yf.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // 表示这是一个控制器
public class indexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                        Model model)
    {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        List<Tag> tags = tagService.listTagTop(10);
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @RequestMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam(value = "query", required = false) String query, Model model)
    {
        model.addAttribute("page", blogService.listBlog(pageable, "%" + query + "%"));
        model.addAttribute("query", query);
        return "search";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model)
    {
        model.addAttribute("blog",blogService.getContentAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newBlogs(Model model)
    {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
