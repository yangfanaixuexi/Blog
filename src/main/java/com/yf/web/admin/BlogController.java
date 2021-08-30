package com.yf.web.admin;

import com.yf.po.Blog;
import com.yf.po.User;
import com.yf.service.BlogService;
import com.yf.service.TagService;
import com.yf.service.TypeService;
import com.yf.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        if(typeService.listType() != null)
        {
            model.addAttribute("types",typeService.listType());
        }

        if(blogService.listBlog(pageable, blog) != null)
        {
            model.addAttribute("page",blogService.listBlog(pageable, blog));
        }

        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 6, sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("page",blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList"; // 返回admin/blogs中的blogList的fragment，实现局部的blogList渲染
    }

    private void setTypeaAndTag(Model model)
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model)
    {
        // 先new一个空对象,防止报错
        model.addAttribute("blog",new Blog());
        // 初始化标签和分类,将文章的标签与分类注入进来
        this.setTypeaAndTag(model);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        // 初始化标签和分类,将文章的标签与分类注入进来
        this.setTypeaAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.Init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session)
    {

        if(blog.getFlag().equals(""))
        {
            blog.setFlag("原创");
        }

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds())); // 在这里设置标签的
        if(blog.getId() != null)
        {
            Blog isBlog = blogService.updateBlog(blog.getId(), blog);
            if(isBlog == null)
            {
                attributes.addFlashAttribute("message","修改文章失败");
            }
            else
            {
                attributes.addFlashAttribute("message","修改文章成功");
            }
        }

        else
        {
            Blog isBlog = blogService.saveBlog(blog);
            if(isBlog == null)
            {
                attributes.addFlashAttribute("message","新增文章失败");
            }
            else
            {
                attributes.addFlashAttribute("message","新增文章成功");
            }
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes)
    {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
