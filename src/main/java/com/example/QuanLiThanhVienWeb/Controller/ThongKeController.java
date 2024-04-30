package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

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
        ArrayList<ThongTinSD> dsvao = getDSVAO();
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (ThongTinSD tt:dsvao){
            ArrayList<String> s = new ArrayList<>();
            s.add(String.valueOf(tt.getMaTV()));
            s.add(getNameByID(tt.getMaTV()));
            s.add(getKhoaByID(tt.getMaTV()));
            s.add(getNganhByID(tt.getMaTV()));
            s.add(tt.getTgVao());
            listData.add(s);
        }
        m.addAttribute("data",listData);
        m.addAttribute("cbKhoa",getDSKhoa());
        m.addAttribute("cbNganh",getDSNganh());
        return "ThongKeTTSDView";
    }

    @PostMapping("/ThongKe/TKTTSD/searchTKTTSD")
    public String handleSeachSubmit(
            @RequestParam("ngaybd") String ngaybd,
            @RequestParam("ngaykt") String ngaykt,
            @RequestParam("cbkhoa") String khoa,
            @RequestParam("cbnganh") String nganh,
            Model model) {

        // Xử lý giá trị được chọn
        ArrayList<ThongTinSD> lstFound = loc(ngaybd,ngaykt,khoa,nganh);
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (ThongTinSD tt:lstFound){
            ArrayList<String> s = new ArrayList<>();
            s.add(String.valueOf(tt.getMaTV()));
            s.add(getNameByID(tt.getMaTV()));
            s.add(getKhoaByID(tt.getMaTV()));
            s.add(getNganhByID(tt.getMaTV()));
            s.add(tt.getTgVao());
            listData.add(s);
        }
        model.addAttribute("data",listData);
        model.addAttribute("cbKhoa",getDSKhoa());
        model.addAttribute("cbNganh",getDSNganh());
        model.addAttribute("ngaybd",ngaybd);
        model.addAttribute("ngaykt",ngaykt);
        model.addAttribute("cbkhoa",khoa);
        model.addAttribute("cbnganh",nganh);
        return "ThongKeTTSDView"; // Trang hiển thị kết quả
    }


    private ArrayList<ThongTinSD> getDSTVByStart(String startDateTime){
        //Datetime: yyyy-mm-ddThh:mm

        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeStart = LocalDateTime.parse(formattedDateTime,formatter);

        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgVao() != null) {
                LocalDateTime dt = LocalDateTime.parse(tt.getTgVao(),formatter);
                if (dt.isEqual(datetimeStart) || dt.isAfter(datetimeStart)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }
    private ArrayList<ThongTinSD> getDSTVByEnd(String endDateTime){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeEnd = LocalDateTime.parse(formattedDateTime,formatter);
        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgVao() != null) {
                LocalDateTime dt = LocalDateTime.parse(tt.getTgVao(),formatter);
                if (dt.isEqual(datetimeEnd) || dt.isBefore(datetimeEnd)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }
    private ArrayList<ThongTinSD> getDSVAO(){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgVao() != null){
                lstFound.add(tt);
            }
        }
        return  lstFound;
    }
    private ArrayList<ThongTinSD> getDSThanhVienByKhoa(String khoa){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        for (ThongTinSD tt: ttRe.findAll()){
            Optional<ThanhVien> optionalThanhVien = tvRe.findById((int) tt.getMaTV());
            ThanhVien tv = optionalThanhVien.get(); // Cẩn thận vì có thể gây ra ngoại lệ
            if (tt.getTgVao() != null) {
                if (tv.getKhoa().contains(khoa)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }
    private ArrayList<ThongTinSD> getDSTVBYNganh(String nganh){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        for (ThongTinSD tt: ttRe.findAll()){
            Optional<ThanhVien> optionalThanhVien = tvRe.findById((int) tt.getMaTV());
            ThanhVien tv = optionalThanhVien.get(); // Cẩn thận vì có thể gây ra ngoại lệ
            if (tt.getTgVao() != null) {
                if (tv.getNganh().contains(nganh)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }
    private ArrayList<String> getDSNganh(){
        HashSet<String> uniqueNganhsSet = new HashSet<>();
        uniqueNganhsSet.add("All");
        // Iterate over the ThanhVien objects and add their khoa values to the uniqueKhoas list
        for (ThanhVien thanhVien : tvRe.findAll()) {
            uniqueNganhsSet.add(thanhVien.getNganh());
        }
        // Convert the HashSet to an ArrayList
        return new ArrayList<>(uniqueNganhsSet);
    }
    private ArrayList<String> getDSKhoa(){
        HashSet<String> uniqueKhoasSet = new HashSet<>();
        uniqueKhoasSet.add("All");
        // Iterate over the ThanhVien objects and add their khoa values to the uniqueKhoas list
        for (ThanhVien thanhVien : tvRe.findAll()) {
            uniqueKhoasSet.add(thanhVien.getKhoa());
        }
        // Convert the HashSet to an ArrayList
        return new ArrayList<>(uniqueKhoasSet);
    }
    private String getNameByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getHoTen();
        }
        return "";
    }
    private String getKhoaByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getKhoa();
        }
        return "";
    }
    private String getNganhByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getNganh();
        }
        return "";
    }
    //Lay phan giao
    private ArrayList<ThongTinSD> layPhanGiao(ArrayList<ThongTinSD>... lists) {
        // Khởi tạo một HashSet để chứa các phần tử chung
        HashSet<ThongTinSD> commonElementsSet = new HashSet<>(lists[0]);
        // Duyệt qua từng ArrayList trong danh sách đầu vào
        for (int i = 1; i < lists.length; i++) {
            // Tạo một HashSet tạm thời chứa phần tử của ArrayList hiện tại
            HashSet<ThongTinSD> currentListSet = new HashSet<>(lists[i]);
            // Giữ lại các phần tử chung với commonElementsSet
            commonElementsSet.retainAll(currentListSet);
        }
        // Chuyển đổi HashSet thành ArrayList và trả về
        return new ArrayList<>(commonElementsSet);
    }
    public ArrayList<ThongTinSD> loc(String ngaybd, String ngaykt, String khoa, String nganh){
        ArrayList<ThongTinSD> theoNgayBD = getDSVAO();
        ArrayList<ThongTinSD> theoNgayKT =  getDSVAO();
        ArrayList<ThongTinSD> theoKhoa =  getDSVAO();
        ArrayList<ThongTinSD> theoNganh =  getDSVAO();

        if (!ngaybd.equals("")){
            theoNgayBD = new ArrayList<>();
            theoNgayBD = getDSTVByStart(ngaybd);
        }
        if (!ngaykt.equals("")){
            theoNgayKT = new ArrayList<>();
            theoNgayKT = getDSTVByEnd(ngaykt);
        }
        if (!khoa.equals("All")){
            theoKhoa = new ArrayList<>();
            theoKhoa = getDSThanhVienByKhoa(khoa);
        }
        if (!nganh.equals("All")){
            theoNganh = new ArrayList<>();
            theoNganh =getDSTVBYNganh(nganh);
        }
        for (ThongTinSD tt : theoNganh) System.out.println(tt.toString());
        return layPhanGiao(theoNgayBD,theoNgayKT,theoKhoa,theoNganh);
    }
}
