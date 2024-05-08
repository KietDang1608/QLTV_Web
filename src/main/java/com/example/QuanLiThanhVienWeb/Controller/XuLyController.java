package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.XuLyRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class XuLyController {
    @Autowired
    private XuLyRepository xlRe;
    private static ArrayList<XuLy> listXL = new ArrayList();

    @GetMapping("/QLXuLy")
    public String getAll(Model m){
        listXL.clear();
        Iterable<XuLy> list = xlRe.findAll();
        for (XuLy xl : list){
            listXL.add(xl);
        }
        m.addAttribute("data",list);
        return "XuLyView";
    }

    //Thêm xử lý
    @GetMapping(value = "/addXuLy")
    
    public String showAddForm(Model model){
        XuLy xl = new XuLy();
        model.addAttribute("xuly", xl);
        return "addXuLy";
    }

    @RequestMapping(value ={"QLXuLy/save"}, method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("xuly") XuLy xl) {
        int MaXL = xl.getMaXL();
        int MaTV = xl.getMaTV();
        String hinhThucXL = xl.getHinhThucXL();
        int soTien = xl.getSoTien();
        String ngayXL = xl.getNgayXL();
        int trangThaiXL = xl.getTrangThaiXL();

        xlRe.save(xl);
        Iterable<XuLy> list = xlRe.findAll();
        model.addAttribute("list", list);
        return "redirect:/QLXuLy";

        
        
    }

    @GetMapping(value = {"QLXuLy/edit/{maXL}"})
    public String showEditForm(@PathVariable("maXL") int maXL, Model model) {

        model.addAttribute("xuly", maXL);
        return "editXuLy";
    }

    public XuLy getXLByMaXL(int maXL){
        for (XuLy xl : xlRe.findAll()){
            if (xl.getMaXL() == maXL)
                return xl;
        }
        return null;
    }
    
    private int findIndexOfXuLy(int maXL) {
        for (int i = 0; i < listXL.size(); i++) {
            if (listXL.get(i).getMaXL() == maXL) {
                return i;
            }
        }
        return -1;

    }

    @PostMapping(value = {"QLXuLy/update"})
    public String update(Model model, @RequestParam("xuly") int tbNew, @RequestParam("trangthaiXL") int trangThaiXuLy) {
        int maXL = tbNew;
        System.out.println(maXL);
        int index = this.findIndexOfXuLy(maXL);
        if (index >= 0) {
            XuLy xl_Old = new XuLy();
            for (XuLy temp : listXL){
                if (temp.getMaXL() == maXL){
                    xl_Old = temp;
                    break;
                }
            }
            System.out.println(xl_Old.getMaXL());
            xl_Old.setTrangThaiXL(trangThaiXuLy); // Sửa đổi ở đây
            listXL.set(index, xl_Old);
            xlRe.save(xl_Old);
        }
        return "redirect:/QLXuLy";
    }
}
