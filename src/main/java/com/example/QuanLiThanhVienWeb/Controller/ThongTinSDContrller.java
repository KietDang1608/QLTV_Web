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
import java.util.Objects;


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
    public String getAllThietBi(Model m, @RequestParam("maTV") int maTV) {
        Iterable<ThietBi> list = tbRepository.findAll();
        listTB = (ArrayList<ThietBi>) list;
        m.addAttribute("data", listTB);

        Iterable<ThongTinSD> list_T = ttsdRepository.findAll();
        listTT = (ArrayList<ThongTinSD>) list_T;
        m.addAttribute("data_T", listTT);

        ArrayList<ThongTinSD> listTT_datcho = new ArrayList();
        ArrayList<ThongTinSD> listTT_muon = new ArrayList();
        for (ThongTinSD tt : listTT){
            if (tt.getMaTV() == maTV){
                if(tt.getTgDatcho() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTgDatcho(), formatter);
                    //Dang Dat Cho
                    if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)) && tt.getTgMuon() == null)
                        listTT_datcho.add(tt);
                }
                //Dang Muon
                if (tt.getMaTV() == maTV && tt.getTgMuon() != null && tt.getTgTra() == null)
                    listTT_muon.add(tt);
            }
        }
        m.addAttribute("dataTT_datcho", listTT_datcho);
        m.addAttribute("dataTT_muon", listTT_muon);
        System.out.println("listTT_muon: " + listTT_muon);
        return "userThietbiView";
    }

    public void deleteDatChoAfter1Hour() {
        service.findDatChoAfter1HourSerVice();
    }

    @RequestMapping(value = {"datcho"}, method = RequestMethod.POST)
    public String saveDatCho(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
                             @RequestParam("maTB") int maTB, @RequestParam("maTV") int maTV) {

        //Kiểm tra thiết bị có đang cho mượn hoặc đặt chỗ không
        if (!isDatChoByMaTB(maTB)){
            System.out.println("Thiết bị có thể đặt chỗ" + isDatChoByMaTB(maTB));
            //Lấy thời gian hiện tại
            ttsd.setTgDatcho(takeCurrentDay());
            String tg_datcho = ttsd.getTgDatcho();
            System.out.println(tg_datcho);

            ttsd.setTgMuon(null);
            String tg_muon = ttsd.getTgMuon();

            //ttsd.setMaTB(maTB);
            //ThongTinSD last= getLastTBByMaLoai(maLoaiTB);
            ttsd = new ThongTinSD(maTV, maTB, tg_muon, tg_datcho);
            ttsdRepository.save(ttsd);
            //        Iterable<ThongTinSD> list = ttsdRepository.findAll();
            //        model.addAttribute("data", list);
        } else {
            System.out.println("khong the dat cho luc nay");
        }
        return "redirect:/userThietbi?maTV=" + maTV;
    }



    //Hàm mượn thiết bị
    @RequestMapping(value = {"muon"}, method = RequestMethod.POST)
    public String saveMuon(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
                           @RequestParam("maTB") int maTB, @RequestParam("maTV") int maTV) {
        int index = findIndexOfTTSD(maTV, maTB, "Muon");
        System.out.println("index: " + index);
        //Nếu đang đặt chỗ thì cập nhật trong db
        if (isDatChoYourSelf(maTV, maTB) && index >= 0){
            ttsd = listTT.get(index);
            ttsd.setTgMuon(takeCurrentDay());
            System.out.println(ttsd.getTgMuon());
            listTT.set(index, ttsd);
            ttsdRepository.save(ttsd);
        } else if (checkMuon(maTB)){
            //Nếu thiết bị ko có ai đang đặt hoặc mượn
            //thì thêm dòng mới vào csdl
            ttsd.setTgMuon(takeCurrentDay());
            ttsd = new ThongTinSD(maTV, maTB, ttsd.getTgMuon());
            ttsdRepository.save(ttsd);
        } else {
            System.out.println("khong the muon luc nay");
        }
        return "redirect:/userThietbi?maTV=" + maTV;
    }

    //Hàm trả thiết bị
    @RequestMapping(value = {"tra"}, method = RequestMethod.POST)
    public String saveTra(Model model, @ModelAttribute("thongtinsd") ThongTinSD ttsd,
                          @RequestParam("maTB") int maTB, @RequestParam("maTV") int maTV) {
        int index = findIndexOfTTSD(maTV, maTB, "Tra");
        System.out.println("maTV: " + maTV + " maTB: " + maTB + " index: " + index);
        //Nếu đang đặt chỗ thì cập nhật trong db
        if (checkTra(maTV, maTB) && index >= 0){
            ttsd = listTT.get(index);
            ttsd.setTgTra(takeCurrentDay());
            listTT.set(index, ttsd);
            ttsdRepository.save(ttsd);
        }else {
            System.out.println("Ban khong muon thiet bi nay");
        }
        return "redirect:/userThietbi?maTV=" + maTV;
    }

    @PostMapping("/QLDatCho/searchTBbyName")
    public String handleSeachSubmit(
        @RequestParam("search") String search,
            Model model, @RequestParam("maTV") int maTV) {
                ArrayList<ThietBi> lstFound = new ArrayList<>();
                for(ThietBi tb: tbRe.findAll()) {
                	if(tb.getTenTB().toLowerCase().contains(search.toLowerCase())) {
                		lstFound.add(tb);
                	}
                }
            
        model.addAttribute("data",lstFound);
        model.addAttribute("search",search);
        
        return "/userThietbiView";
    }

    @GetMapping("/QLDatCho/refresh")
    public String rf(Model m, @RequestParam("maTV") int maTV) {

        getAllThietBi(m, maTV);

        return "redirect:/userThietbi?maTV=" + maTV;

    }


    public Boolean isDatChoByMaTB(Integer maTB) {
        for (ThongTinSD tt : ttsdRepository.findAll()) {
            // Nếu có thiết bị trong db
            if (tt.getMaTB() == maTB && tt.getTgDatcho() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTgDatcho(), formatter);
                if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)))
                    return true;
            }
        }
        // Nếu không tìm thấy mã thiết bị
        return false;
    }

    private int findIndexOfTTSD(int maTV, int maTB, String state) {
        for (int i = 0; i < listTT.size(); i++) {
            if (listTT.get(i).getMaTV() == maTV && listTT.get(i).getMaTB() == maTB) {
                if (state.equals("Muon") && listTT.get(i).getTgMuon() == null) {
                    return i;
                } else if (state.equals("Tra") && listTT.get(i).getTgTra() == null){
                    System.out.println("CHECKING" + listTT.get(i).getTgTra());
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean isDatChoYourSelf(Integer maTV, Integer maTB){
        //mình đang đặt chỗ
        for (ThongTinSD tt : ttsdRepository.findAll()){
            if (tt.getMaTV() == maTV && tt.getMaTB() == maTB && tt.getTgDatcho() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTgDatcho(), formatter);
                if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)))
                    return true;
            }
        }
        return false;
    }

    public boolean checkMuon(Integer maTB) {
        for (ThongTinSD tt : ttsdRepository.findAll()) {
            if (tt.getMaTB() == maTB){
                if (isDatChoByMaTB(maTB) || (tt.getTgMuon() != null && tt.getTgTra() == null)) {
                    // có ai đang đặt chỗ hoặc đang mượn
                    // thì ko được mượn
                    return false;
                }
            }
        }
        //Thiết bị đang trống có thể mượn
        return true;
    }

    public boolean checkTra(Integer maTV, Integer maTB){
        for (ThongTinSD tt : ttsdRepository.findAll()) {
            if (tt.getMaTV() == maTV && tt.getMaTB() == maTB){
                if (tt.getTgMuon() != null && tt.getTgTra() == null) {
                    return true;
                }
            }
        }
        return false;
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
