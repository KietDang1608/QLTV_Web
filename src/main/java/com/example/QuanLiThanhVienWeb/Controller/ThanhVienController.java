package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;

    @GetMapping("/all")
    public String getAll(Model m){
        Iterable<ThanhVien> list = tvRe.findAll();
        m.addAttribute("data",list);
        return "index";
    }
}
