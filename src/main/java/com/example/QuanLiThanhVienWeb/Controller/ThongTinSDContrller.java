package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThietBi;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;

import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private ScheduledTasks service;

    private static ArrayList<ThongTinSD> listTT = new ArrayList();
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

    public void deleteDatChoAfter1Hour() {
        service.findDatChoAfter1HourSerVice();
    }

    @RequestMapping(value = {"datcho"}, method = RequestMethod.POST)
    public String saveDatCho(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
    @RequestParam("maTB") int maTB) {

        //Kiểm tra thiết bị có đang cho mượn hoặc đặt chỗ không
        if (!isDatChoByMaTB(maTB)){
            System.out.println("Thiết bị có thể đặt chỗ" + isDatChoByMaTB(maTB));
            //Lấy thời gian hiện tại
            ttsd.setTGDatcho(takeCurrentDay());
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

        } else {
            System.out.println("khong the dat cho luc nay");
        }
        return "userThietbiView";
    }



    //Hàm mượn thiết bị
    @RequestMapping(value = {"muon"}, method = RequestMethod.POST)
    public String saveMuon(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
    @RequestParam("maTB") int maTB, @RequestParam("maTV") int maTV) {
        if (checkMuon(maTV, maTB)) { //thiết bị chưa được mượn bao giờ hoặc đã được mượn và hiện đang trống
            ttsd.setTgMuon(takeCurrentDay());
            ttsd = new ThongTinSD(maTB, ttsd.getTgMuon());
            ttsdRepository.save(ttsd);
        } else {
            System.out.println("khong the muon luc nay");
        }
        return "userThietbiView";
    }

    //Hàm trả thiết bị


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


    public Boolean isDatChoByMaTB(Integer maTB) {
        for (ThongTinSD tt : ttsdRepository.findAll()) {
            // Nếu có thiết bị trong db
            if (tt.getMaTB() == maTB) {
                return tt.getTGDatcho() != null || tt.getTgMuon() != null;
            }
        }
        // Nếu không tìm thấy mã thiết bị
        return false;
    }

    public boolean checkMuon(Integer maTV, Integer maTB) {
        for (ThongTinSD tt : ttsdRepository.findAll()) {
            if (tt.getMaTB() == maTB){
                //mình đang đặt chỗ
                if (tt.getMaTV() == maTV && tt.getTGDatcho() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTgMuon(), formatter);
                    if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)))
                        return true;
                } else if (isDatChoByMaTB(maTB) || (tt.getTgMuon() != null && tt.getTgTra() == null)) {
                    // có ai đang đặt chỗ hoặc đang mượn
                    // thì ko được mượn
                    return false;
                }
            }
        }
        //Thiết bị đang trống có thể mượn
        return true;
    }


//    public DatetoTime(){
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date();
//        return formatter.format(date);
//    }
    public String takeCurrentDay(){
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDate.format(formatter);
    }
}
