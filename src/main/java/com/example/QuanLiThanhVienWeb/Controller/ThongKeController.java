package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class ThongKeController {
    @Autowired
    private ThongTinSDRepository ttRe;

    @Autowired ThanhVienRepository tvRe;
    @RequestMapping("/ThongKe")
    public String toThongKe(){
        return "ThongKeView";
    }
    @GetMapping("/ThongKe/TKTTSD")
    public String TKVaoKVHTAll(Model m){
        Iterable<ThongTinSD> list = ttRe.findAll();
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (ThongTinSD tt:list){
            ArrayList<String> s = new ArrayList<>();
            s.add(String.valueOf(tt.getMaTV()));
            s.add(getNameByID(tt.getMaTV()));
            s.add(getKhoaByID(tt.getMaTV()));
            s.add(getNganhByID(tt.getMaTV()));
            s.add(tt.getTgVao());

            listData.add(s);
        }
        m.addAttribute("data",listData);
        return "ThongKeTTSDView";
    }

    public String getNameByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getHoTen();
        }
        return "";
    }
    public String getKhoaByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getKhoa();
        }
        return "";
    }
    public String getNganhByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getNganh();
        }
        return "";
    }
}
