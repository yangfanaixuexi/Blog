package com.yf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class aboutShowController {

    @GetMapping("/about")
    public String about()
    {
        return "about";
    }
}
