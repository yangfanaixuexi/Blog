package com.yf.web.admin;

import com.yf.po.Type;
import com.yf.service.TypeService;
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
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String list(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        typeService.listType(pageable);
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model)
    {
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @Transactional
    @GetMapping(value = "/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        Type type = typeService.getType(id);
        // 报错是因为出现了懒加载的问题,即: 虽然我拿到了
        model.addAttribute("type", type);
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type,BindingResult result, RedirectAttributes attributes)
    {
        Type t1 = typeService.getTypeByName(type.getName());
        if(t1 != null)
        {
            result.rejectValue("name","nameError","分类名称已存在");   //自定义向result验证器加入错误
        }

        if(result.hasErrors())
        {
            return "admin/types-input";
        }

        Type t = typeService.saveType(type);
        if(t == null)
        {
            attributes.addFlashAttribute("message","新增失败");
        }
        else
        {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,BindingResult result,@PathVariable Long id, RedirectAttributes attributes)
    {
        Type t1 = typeService.getTypeByName(type.getName());
        System.out.println(t1);
        if(t1 != null)
        {
            result.rejectValue("name","nameError","分类名称已存在");   //自定义向result验证器加入错误
        }

        if(result.hasErrors())
        {
            return "admin/types-input";
        }

        Type t = typeService.updateType(id, type);
        if(t == null)
        {
            attributes.addFlashAttribute("message","更新失败");
        }
        else
        {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes)
    {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
