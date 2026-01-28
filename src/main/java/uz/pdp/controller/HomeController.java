package uz.pdp.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/homeModel")
    public String homeModel() {
        return "homeModel";
    }
}
