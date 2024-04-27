package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
@Controller
public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;

    @GetMapping("/QLThanhVien")
    public String getAll(Model m){
        Iterable<ThanhVien> list = tvRe.findAll();
        m.addAttribute("data",list);
        for (ThanhVien tv : list){
            System.out.println(tv.getEmail());
        }
        return "ThanhVienView";
    }

}
