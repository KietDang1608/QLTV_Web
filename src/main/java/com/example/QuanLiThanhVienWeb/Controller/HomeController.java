package com.example.QuanLiThanhVienWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String toHome(){
        return "home";
    }

    @RequestMapping("/home")
    public String profileToHome(@RequestParam String hoTen, @RequestParam String maTV, Model m){
        m.addAttribute("username", hoTen);
        m.addAttribute("maTV", maTV);
        return "home";
    }
}
