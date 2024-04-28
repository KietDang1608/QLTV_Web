package com.example.QuanLiThanhVienWeb.Controller;
import com.example.QuanLiThanhVienWeb.Entity.ThietBi;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;

@Controller
public class ThietBiController {
    @Autowired
    private ThietBiRepository tbRe;
    private static ArrayList<ThietBi> listTB = new ArrayList();
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
    public String save(Model model, @ModelAttribute("thietbi") ThietBi tb) {
        int id = tb.getMaTB();
        String tentb = tb.getTenTB();
        String mota = tb.getMoTa();
        ThietBi tb2 = new ThietBi(tentb, mota);
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

}
