package com.example.QuanLiThanhVienWeb.Controller;
import com.example.QuanLiThanhVienWeb.Entity.ThietBi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;

@Controller
public class ThietBiController {
    @Autowired
    private ThietBiRepository tbRe;
    private static ArrayList<ThietBi> listTB = new ArrayList();
    @RequestMapping("/QLThietBi")
    public String toThietBi(){
        return "ThietBiView";
    }
    @GetMapping("/QLThietBi")
    public String getAll(Model m){
        Iterable<ThietBi> list = tbRe.findAll();
        listTB=(ArrayList<ThietBi>) list;
        m.addAttribute("data",list);
        // for (ThietBi tv : list){
        //     System.out.println(tv.getEmail());
        // }
        return "ThietBiView";
    }

    @RequestMapping(value = "addTB")
    public String showAddForm(Model model) {
        ThietBi tb = new ThietBi();
        model.addAttribute("thietbi", tb);
        return "addTB";
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute("thietbi") ThietBi tb,
    @RequestParam("cbtenTB") int maLoaiTB) {
        int id = tb.getMaTB();
        String tentb = tb.getTenTB();
        String mota = tb.getMoTa();
        
        ThietBi last=getLastTBByMaLoai(maLoaiTB);
        ThietBi tb2 = new ThietBi(last.getMaTB()+1,tentb, mota);
        System.out.println(tb2.getMaTB());
        tbRe.save(tb2);
        Iterable<ThietBi> list = tbRe.findAll();
        model.addAttribute("list", list);
        return "redirect:/QLThietBi";
    }

    @GetMapping(value = {"QLThietBi/edit/{id}"})
    public String showEditForm(@PathVariable("id") int id, Model model) {
        ThietBi tb = listTB.get(id);
        model.addAttribute("thietbi", tb);
        return "editTB";
    }

    public ThietBi getTBByID(int id){
        for (ThietBi tb : tbRe.findAll()){
            if (tb.getMaTB() == id)
                return tb;
        }
        return null;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute("thietbi") ThietBi tbNew) {
        int maTB = tbNew.getMaTB();
        int index = this.findIndexOfThietBi(maTB);
        if (index >= 0) {
            ThietBi tbOld = listTB.get(index);
            tbOld.setTenTB(tbNew.getTenTB());
            tbOld.setMoTa(tbNew.getMoTa());
            System.out.println(tbOld.getTenTB());
            listTB.set(index, tbOld);
            tbRe.save(tbOld);
        }
        return "redirect:/QLThietBi";
    }

    private int findIndexOfThietBi(int id) {
        for (int i = 0; i < listTB.size(); i++) {
            if (listTB.get(i).getMaTB() == id) {
                return i;
            }
        }
        return -1;

    }

    @GetMapping(value = {"QLThietBi/delete/{id}"})
    public String deleteTB(@PathVariable("id") int id, Model model) {
        ThietBi tb= listTB.get(id);
        tbRe.delete(tb);
        return "redirect:/QLThietBi";
    }

    @PostMapping(value = "/QLThietBi/deleteSelected")
    public String deleteSelectedTB(@RequestParam("selectedItems") String selectedItems) {
        // Chia chuỗi selectedItems thành mảng các maTB
        String[] maTBs = selectedItems.split(",");
        for (String maTB : maTBs) {
            // Chuyển đổi maTB từ String sang int
            int id = Integer.parseInt(maTB);
            System.out.println(id);
            ThietBi tb = getTBByID(id);
            tbRe.delete(tb);
        }
        return "redirect:/QLThietBi";
    }

    @PostMapping("/QLThietBi/searchTB")
    public String handleSeachSubmit(
        @RequestParam("cbtenTB") int maLoaiTB,
        @RequestParam("mota") String mota,
            Model model) {
                ArrayList<ThietBi> lstFound = new ArrayList<>();
            if(maLoaiTB==0)
            {
                lstFound=(ArrayList<ThietBi>) tbRe.findAll();
            }
            else{
                lstFound = getDSTBBYMaLoai(maLoaiTB,mota);
            }
        model.addAttribute("data",lstFound);
        model.addAttribute("cbtenTB",maLoaiTB);
        model.addAttribute("mota",mota);
        
        return "ThietBiView"; // Trang hiển thị kết quả
    }


    private ArrayList<ThietBi> getDSTBBYMaLoai(int maLoaiTB,String mota){
        ArrayList<ThietBi> lstFound = new ArrayList<>();
        for (ThietBi tb: tbRe.findAll()){
            String matbString=String.valueOf(tb.getMaTB());
            String sodau=matbString.substring(0,1);
            if(sodau.equals(String.valueOf(maLoaiTB)))
            {
                if (tb.getMoTa().contains(mota)) {
                    lstFound.add(tb);
                }
            }
        }
        return lstFound;
    }

    private ThietBi getLastTBByMaLoai(int maLoaiTB){
        ThietBi kq = new ThietBi();
        ThietBi maxThietBi = null;
        for (ThietBi tb: tbRe.findAll()){
            String matbString=String.valueOf(tb.getMaTB());
            String sodau=matbString.substring(0,1);
            if(sodau.equals(String.valueOf(maLoaiTB)))
            {
                String phanconLai=matbString.substring(1,5);
                if(Integer.parseInt(phanconLai)==LocalDate.now().getYear())
                {
                if (maxThietBi == null || Integer.parseInt(matbString) > Integer.parseInt(String.valueOf(maxThietBi.getMaTB()))) {
                    maxThietBi = tb;
                }
                }
            }
        }
        if (maxThietBi != null) 
        {
            System.out.println(maxThietBi.getMaTB()+1);
            kq = maxThietBi;
        }
        return kq;
    }

}
