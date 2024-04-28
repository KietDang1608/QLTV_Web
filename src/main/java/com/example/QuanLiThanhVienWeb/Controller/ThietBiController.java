package com.example.QuanLiThanhVienWeb.Controller;
import com.example.QuanLiThanhVienWeb.Entity.ThietBi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;

@Controller
public class ThietBiController {
    @Autowired
    private ThietBiRepository tbRe;

    @GetMapping("/QLThietBi")
    public String getAll(Model m){
        Iterable<ThietBi> list = tbRe.findAll();
        m.addAttribute("data",list);
        // for (ThietBi tv : list){
        //     System.out.println(tv.getEmail());
        // }
        return "ThietBiView";
    }
}
