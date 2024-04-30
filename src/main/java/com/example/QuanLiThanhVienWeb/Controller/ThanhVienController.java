package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;

    @RequestMapping("/changePassword")
    public String toChangePassword(){return "doiMatKhau";}

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
    public ResponseEntity<?> saveNewPass(
            @RequestParam long userId,
            @RequestParam String password,
            @RequestParam String confirmedPass
    ){
        try {
            if (password.equals(confirmedPass)){
                ThanhVien thanhVien = tvRe.findById(userId).orElseThrow(null);
                if (thanhVien == null){
                    System.out.println("Error not found user");
                    return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
                }
                thanhVien.setPassword(password);
                tvRe.save(thanhVien);
                System.out.println("Password changed successfully");
                return new ResponseEntity<>("Password changed successfully!", HttpStatus.OK);
            }
            else {
                System.out.println("Error passwords are not the same");
                return new ResponseEntity<>("Password and password confirmed must be the same", HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            System.out.println("Error "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
