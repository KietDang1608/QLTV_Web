package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.XuLyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class XuLyController {
    @Autowired
    private XuLyRepository xlRe;

    @GetMapping("/QLXuLy")
    public String getAll(Model m){
        Iterable<XuLy> list = xlRe.findAll();
        m.addAttribute("data",list);
        return "XuLyView";
    }

}
