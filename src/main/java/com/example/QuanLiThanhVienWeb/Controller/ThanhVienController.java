package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.XuLyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;
    @Autowired
    private XuLyRepository xlRe;

    @GetMapping("/changePassword")
    public String toChangePassword(@RequestParam(name = "maTV") String maTV, Model model){
        model.addAttribute("maTV", maTV);
        return "doiMatKhau";
    }

    @GetMapping("/QLThanhVien")
    public String getAll(Model m){
        Iterable<ThanhVien> list = tvRe.findAll();
        m.addAttribute("data",list);
        for (ThanhVien tv : list){
            System.out.println(tv.getHoTen());
        }
        return "ThanhVienView";
    }

    @PostMapping("/save-password")
    public String saveNewPass(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam long maTV,
            @RequestParam String oldPass,
            @RequestParam String password,
            @RequestParam String confirmedPass
    ){
        try {
            Iterable<ThanhVien> list = tvRe.findAll();
            for (ThanhVien us : list){
                if(us.getMaTV() == maTV && us.getPassword().equals(oldPass)){
                    if (password.equals(confirmedPass)){
                        ThanhVien thanhVien = tvRe.findById(maTV).orElseThrow(null);
                        if (thanhVien == null){
                            System.out.println("Error not found user");
                            model.addAttribute("errorMessage", "Không tìm thấy thành viên!");
                            model.addAttribute("maTV", maTV);
                            String returnUrl = "/changePassword?maTV=" + maTV;
                            return "redirect:" + returnUrl;
                        }
                        thanhVien.setPassword(password);
                        tvRe.save(thanhVien);
                        model.addAttribute("errorMessage", "Đổi mật khẩu thành công!");
                        model.addAttribute("maTV", maTV);
                        String returnUrl = "/profile?maTV=" + maTV;
                        return "redirect:" + returnUrl;
                    }
                }
            }
            model.addAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            model.addAttribute("maTV", maTV);
            redirectAttributes.addAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            String returnUrl = "/changePassword?maTV=" + maTV;
            return "redirect:" + returnUrl;
        }
        catch (Exception e){
            System.out.println("Error "+e.getMessage());
            redirectAttributes.addAttribute("errorMessage", "Lỗi ThanhVienController");
            String returnUrl = "/changePassword?maTV=" + maTV;
            return "redirect:" + returnUrl;
        }
    }

    @GetMapping("/profile")
    public String getPersonalInfo(
            Model model,
            @RequestParam long maTV
    ){
        Optional<ThanhVien> data = tvRe.findById(maTV);
        List<XuLy> list = xlRe.findByMaTV(maTV);
        if (data.isPresent()){
            ThanhVien thanhVien = data.get();
            model.addAttribute("maTV", maTV);
            model.addAttribute("hoTen", thanhVien.getHoTen());
            model.addAttribute("khoa", thanhVien.getKhoa());
            model.addAttribute("nganh", thanhVien.getNganh());
            model.addAttribute("sdt", thanhVien.getSdt());
            model.addAttribute("email", thanhVien.getEmail());
            if (!list.isEmpty()){
                model.addAttribute("xuLyList", list);
            }
            return "profile";
        }
        return "home";
    }
}
