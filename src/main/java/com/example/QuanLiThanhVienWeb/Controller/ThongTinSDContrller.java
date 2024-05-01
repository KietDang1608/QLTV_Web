package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThietBi;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;

import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;


@Controller
public class ThongTinSDContrller {
	

    @Autowired
    private ThongTinSDRepository ttsdRepository;
    @Autowired
    private ThietBiRepository tbRepository;
    @Autowired
    private ThietBiRepository tbRe;

    private static ArrayList<ThongTinSDContrller> list = new ArrayList();
    private static ArrayList<ThietBi> listTB = new ArrayList();

    @RequestMapping("/userThietBi")
    public String toUserThietbiView(){
        return "userThietbiView";
    }

    @GetMapping("/userThietbi")
    public String getAllThietBi(Model m) {
        Iterable<ThietBi> list = tbRepository.findAll();
        listTB = (ArrayList<ThietBi>) list;
        m.addAttribute("data", listTB);
        return "userThietbiView";
    }

    @RequestMapping(value = {"datcho"}, method = RequestMethod.POST)
    public String saveDatCho(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
    @RequestParam("maTB") int maTB) {

        //Kiểm tra thiết bị có đang cho mượn hoặc đặt chỗ không
        Boolean inTTSD = ttsdRepository.existsByMaTB(maTB);
        boolean isToday = ttsdRepository.existsByMaTBAndNgayDatCho(maTB);
        System.out.println("nguoi dat cho " + inTTSD);
        System.out.println("nguoi dat cho " + isToday);
        if (!inTTSD || inTTSD & !isToday) { //thiết bị chưa được mượn bao giờ hoặc đã được mượn và hiện đang trống
            //Lấy thời gian hiện tại
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            ttsd.setTGDatcho(formatter.format(date));
            String tg_datcho = ttsd.getTGDatcho();
            System.out.println(tg_datcho);

            ttsd.setTgMuon("null");
            String tg_muon = ttsd.getTgMuon();

            //ttsd.setMaTB(maTB);
            //ThongTinSD last= getLastTBByMaLoai(maLoaiTB);
            ttsd = new ThongTinSD(maTB, tg_muon, tg_datcho);
            ttsdRepository.save(ttsd);
            Iterable<ThongTinSD> list = ttsdRepository.findAll();
            model.addAttribute("list", list);
            return "success";
        } else {
            return "failed";
        }
    }
    
    @PostMapping("/QLDatCho/searchTBbyName")
    public String handleSeachSubmit(
        @RequestParam("search") String search,
            Model model) {
                ArrayList<ThietBi> lstFound = new ArrayList<>();
                for(ThietBi tb: tbRe.findAll()) {
                	if(tb.getTenTB().contains(search)) {
                		lstFound.add(tb);
                	}
                }
            
        model.addAttribute("data",lstFound);
        model.addAttribute("search",search);
        
        return "userThietbiView"; // Trang hiển thị kết quả
    }
    @PostMapping("/QLDatCho/refresh")
    public String rf(Model model) {
    			Iterable<ThietBi> lstFound = tbRe.findAll();
                listTB =(ArrayList<ThietBi>) lstFound;
            
        model.addAttribute("data",lstFound);
        
        return "userThietbiView"; // Trang hiển thị kết quả
    }
}
