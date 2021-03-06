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
        return "admin/blogs :: blogList"; // ??????admin/blogs??????blogList???fragment??????????????????blogList??????
    }

    private void setTypeaAndTag(Model model)
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model)
    {
        // ???new???????????????,????????????
        model.addAttribute("blog",new Blog());
        // ????????????????????????,???????????????????????????????????????
        this.setTypeaAndTag(model);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        // ????????????????????????,???????????????????????????????????????
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
            blog.setFlag("??????");
        }

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds())); // ????????????????????????
        if(blog.getId() != null)
        {
            Blog isBlog = blogService.updateBlog(blog.getId(), blog);
            if(isBlog == null)
            {
                attributes.addFlashAttribute("message","??????????????????");
            }
            else
            {
                attributes.addFlashAttribute("message","??????????????????");
            }
        }

        else
        {
            Blog isBlog = blogService.saveBlog(blog);
            if(isBlog == null)
            {
                attributes.addFlashAttribute("message","??????????????????");
            }
            else
            {
                attributes.addFlashAttribute("message","??????????????????");
            }
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes)
    {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "????????????");
        return REDIRECT_LIST;
    }
}
