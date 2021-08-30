package com.yf.web.admin;

import com.yf.po.Tag;
import com.yf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")    // 地址映射,tag->list方法
    public String list(@PageableDefault(size = 6, sort = {"id"},direction = Sort.Direction.DESC)Pageable pageable, Model model)
    {
        // 这个相当于$this->assign;
        model.addAttribute("page",tagService.listTag(pageable));
        tagService.listTag(pageable);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model)
    {
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @Transactional
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model)
    {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag",tag);
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes)
    {
        Tag findTag = tagService.getTagByName(tag.getName());
        if(findTag != null)
        {
            result.rejectValue("name","nameError","标签名称已存在");
        }

        if(result.hasErrors())
        {
            return "admin/tags-input";
        }

        Tag addTag = tagService.saveTag(tag);
        if(addTag == null)
        {
            attributes.addFlashAttribute("message","新增失败");
        }

        else
        {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes)
    {
        Tag findTag = tagService.getTagByName(tag.getName());
        if(findTag != null)
        {
            result.rejectValue("name","nameError","标签名称已存在");   //自定义向result验证器加入错误
        }

        if(result.hasErrors())
        {
            return "admin/tags-input";
        }

        Tag updateTag = tagService.updateTag(id, tag);
        if(updateTag == null)
        {
            attributes.addFlashAttribute("message","更新失败");
        }
        else
        {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes)
    {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
