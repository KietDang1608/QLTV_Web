package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;

    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @GetMapping("/QLThanhVien")
    public String getAll(Model m) {
        Iterable<ThanhVien> list = tvRe.findAll();
        m.addAttribute("data", list);
        for (ThanhVien tv : list) {
            System.out.println(tv.getEmail());
        }
        return "ThanhVienView";
    }




    //xem thông tin chi tiết của 1 thành viên
    @GetMapping("/thanhvien/{maTV}/thong-tin-dat-cho")
    public String getThongTinDatCho(@PathVariable("maTV") int maTV, Model model) {
        ThanhVien thanhVien = tvRe.findById(maTV).orElse(null);
        if (thanhVien != null) {
            model.addAttribute("thanhVien", thanhVien);

            // Lấy danh sách thông tin đặt chỗ thiết bị
            List<ThongTinSD> thongTinDatChoList = thongTinSDRepository.findByMaTVAndMaTBIsNull(maTV);
            model.addAttribute("thongTinDatChoList", thongTinDatChoList);

            // Lấy danh sách thông tin mượn thiết bị
            List<ThongTinSD> thongTinMuonList = thongTinSDRepository.findByMaTVAndMaTBIsNotNull(maTV);
            model.addAttribute("thongTinMuonList", thongTinMuonList);

            return "thong-tin-dat-cho";
        } else {
            return "Error404"; 
        }
    }

}
