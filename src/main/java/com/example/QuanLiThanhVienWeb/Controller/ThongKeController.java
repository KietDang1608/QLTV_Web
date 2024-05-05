package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThietBi;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import com.example.QuanLiThanhVienWeb.Repositories.XuLyRepository;
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
    @Autowired
    private ThietBiRepository tbRe;
    @Autowired
    private XuLyRepository xlRe;
    
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

    @GetMapping("/ThongKe/TKTB")
    public String TKThietBiMuonALL(Model m){
        ArrayList<ThongTinSD> dsmtb = getDSMTB();
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (ThongTinSD tt:dsmtb){
            ArrayList<String> e = new ArrayList<>();
            e.add(String.valueOf(tt.getMaTB()));
            e.add(getNameTBByID(tt.getMaTB()));
            e.add(getMoTaByID(tt.getMaTB()));
            e.add(tt.getTgMuon());
            listData.add(e);
        }
        m.addAttribute("data",listData);
        return "ThongKeTBDMView";
    }
    @GetMapping("/ThongKe/TKXL")
    public String TKXuLyVPALL(Model m){
        ArrayList<XuLy> dsvp = getDSXL();
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (XuLy xl:dsvp){
            ArrayList<String> e = new ArrayList<>();
            e.add(String.valueOf(xl.getMaXL()));
            e.add(String.valueOf(xl.getMaTV()));
            e.add(xl.getHinhThucXL());
            e.add(String.valueOf(xl.getSoTien()));
            e.add(xl.getNgayXL());
            listData.add(e);
            }
        m.addAttribute("data",listData);
        m.addAttribute("cbXuly",getDSHinhThucXuly());
        return "ThongKeXLVPView";
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

    @PostMapping("/ThongKe/TKTB/searchTKTB")
    public String handleSeachSubmitTB(
            @RequestParam("ngaybd") String ngaybd,
            @RequestParam("ngaykt") String ngaykt,

            Model model) {

        // Xử lý giá trị được chọn
        ArrayList<ThongTinSD> lstFound = loctheongaymuon(ngaybd,ngaykt);
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (ThongTinSD tt:lstFound){
            ArrayList<String> e = new ArrayList<>();
            e.add(String.valueOf(tt.getMaTB()));
            e.add(getNameTBByID(tt.getMaTB()));
            e.add(getMoTaByID(tt.getMaTB()));
            e.add(tt.getTgMuon());
            listData.add(e);
        }
        model.addAttribute("data",listData);
        model.addAttribute("ngaybd",ngaybd);
        model.addAttribute("ngaykt",ngaykt);

        return "ThongKeTBDMView"; // Trang hiển thị kết quả
    }

    @PostMapping("/ThongKe/TKXL/searchTKVP")
    public String handleSeachSubmitXL(
            @RequestParam("ngaybd") String ngaybd,
            @RequestParam("ngaykt") String ngaykt,
            @RequestParam("cbxuly") String xuly,
            Model model) {

        // Xử lý giá trị được chọn
        ArrayList<XuLy> lstFound = locxuly(ngaybd,ngaykt,xuly);
        ArrayList<ArrayList<String>> listData = new ArrayList<>();
        for (XuLy xl:lstFound){
            ArrayList<String> e = new ArrayList<>();
            e.add(String.valueOf(xl.getMaXL()));
            e.add(String.valueOf(xl.getMaTV()));
            e.add(xl.getHinhThucXL());
            e.add(String.valueOf(xl.getSoTien()));
            e.add(xl.getNgayXL());
            listData.add(e);
        }
        model.addAttribute("data",listData);
        model.addAttribute("cbXuly",getDSHinhThucXuly());
        model.addAttribute("ngaybd",ngaybd);
        model.addAttribute("ngaykt",ngaykt);
        model.addAttribute("cbxuly",xuly);
        return "ThongKeXLVPView"; // Trang hiển thị kết quả
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

    private ArrayList<ThongTinSD> getDSTBByStart(String startDateTime){
        //Datetime: yyyy-mm-ddThh:mm

        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeStart = LocalDateTime.parse(formattedDateTime,formatter);

        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgMuon() != null) {
                LocalDateTime dt = LocalDateTime.parse(tt.getTgMuon(),formatter);
                if (dt.isEqual(datetimeStart) || dt.isAfter(datetimeStart)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }
    private ArrayList<XuLy> getDSXLByStart(String startDateTime){
        //Datetime: yyyy-mm-ddThh:mm

        ArrayList<XuLy> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(startDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeStart = LocalDateTime.parse(formattedDateTime,formatter);

        for (XuLy xl: xlRe.findAll()){
            if (xl.getTrangThaiXL() == 1) {
                LocalDateTime dt = LocalDateTime.parse(xl.getNgayXL(),formatter);
                if (dt.isEqual(datetimeStart) || dt.isAfter(datetimeStart)) {
                    lstFound.add(xl);
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

    private ArrayList<ThongTinSD> getDSTBByEnd(String endDateTime){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeEnd = LocalDateTime.parse(formattedDateTime,formatter);
        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgMuon() != null) {
                LocalDateTime dt = LocalDateTime.parse(tt.getTgMuon(),formatter);
                if (dt.isEqual(datetimeEnd) || dt.isBefore(datetimeEnd)) {
                    lstFound.add(tt);
                }
            }
        }
        return lstFound;
    }

    private ArrayList<XuLy> getDSXLByEnd(String endDateTime){
        ArrayList<XuLy> lstFound = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.parse(endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        LocalDateTime datetimeEnd = LocalDateTime.parse(formattedDateTime,formatter);
        for (XuLy xl: xlRe.findAll()){
            if (xl.getTrangThaiXL() == 1) {
                LocalDateTime dt = LocalDateTime.parse(xl.getNgayXL(),formatter);
                if (dt.isEqual(datetimeEnd) || dt.isBefore(datetimeEnd)) {
                    lstFound.add(xl);
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
    private ArrayList<ThongTinSD> getDSMTB(){
        ArrayList<ThongTinSD> lstFound = new ArrayList<>();
        for (ThongTinSD tt: ttRe.findAll()){
            if (tt.getTgMuon() != null){
                lstFound.add(tt);
            }
        }
        return  lstFound;
    }

    private ArrayList<XuLy> getDSXL(){
        ArrayList<XuLy> lstFound = new ArrayList<>();
        for (XuLy xl: xlRe.findAll()){
            if (xl.getTrangThaiXL() == 1){
                lstFound.add(xl);
            }
        }
        return lstFound;
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
    private ArrayList<XuLy> getDSXuLyByHinhThucXuLy(String xuly){
        ArrayList<XuLy> lstFound = new ArrayList<>();
        for (XuLy xl: xlRe.findAll()){
            Optional<XuLy> optionalXuLy = xlRe.findById((long) xl.getMaXL());
            XuLy xly = optionalXuLy.get();
            if (xl.getTrangThaiXL() == 1){
                if (xly.getHinhThucXL().contains(xuly)) {
                    lstFound.add(xl);
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

    private ArrayList<String> getDSHinhThucXuly(){
        HashSet<String> uniqueXulysSet = new HashSet<>();
        uniqueXulysSet.add("All");
        // Iterate over the XuLy objects and add their xuly values to the uniqueXulys list
        for (XuLy xl: xlRe.findAll()) {
            uniqueXulysSet.add(xl.getHinhThucXL());
        }
        // Convert the HashSet to an ArrayList
        return new ArrayList<>(uniqueXulysSet);
    }

    private String getNameByID(int id){
        for (ThanhVien tt : tvRe.findAll()){
            if (tt.getMaTV() == id)
                return tt.getHoTen();
        }
        return "";
    }
    private String getNameTBByID(int id){
        for (ThietBi tb : tbRe.findAll()){
            if (tb.getMaTB() == id)
                return tb.getTenTB();
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

    private String getMoTaByID(int id){
        for (ThietBi tb : tbRe.findAll()){
            if (tb.getMaTB() == id)
                return tb.getMoTa();
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
    private ArrayList<XuLy> layPhanGiaoXL(ArrayList<XuLy>... lists) {
        // Khởi tạo một HashSet để chứa các phần tử chung
        HashSet<XuLy> commonElementsSet = new HashSet<>(lists[0]);
        // Duyệt qua từng ArrayList trong danh sách đầu vào
        for (int i = 1; i < lists.length; i++) {
            // Tạo một HashSet tạm thời chứa phần tử của ArrayList hiện tại
            HashSet<XuLy> currentListSet = new HashSet<>(lists[i]);
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

    public ArrayList<ThongTinSD> loctheongaymuon(String ngaybd, String ngaykt){
        ArrayList<ThongTinSD> theoNgayBD = getDSMTB();
        ArrayList<ThongTinSD> theoNgayKT =  getDSMTB();
        if (!ngaybd.equals("")){
            theoNgayBD = new ArrayList<>();
            theoNgayBD = getDSTBByStart(ngaybd);
        }
        if (!ngaykt.equals("")){
            theoNgayKT = new ArrayList<>();
            theoNgayKT = getDSTBByEnd(ngaykt);
        }
//        for (ThongTinSD tt : theoNganh) System.out.println(tt.toString());
        return layPhanGiao(theoNgayBD,theoNgayKT);
    }

    public ArrayList<XuLy> locxuly(String ngaybd, String ngaykt,String xuly){
        ArrayList<XuLy> theoNgayBD = getDSXL();
        ArrayList<XuLy> theoNgayKT = getDSXL();
        ArrayList<XuLy> theoXuly = getDSXL();
        if (!ngaybd.equals("")){
            theoNgayBD = new ArrayList<>();
            theoNgayBD = getDSXLByStart(ngaybd);
        }
        if (!ngaykt.equals("")){
            theoNgayKT = new ArrayList<>();
            theoNgayKT = getDSXLByEnd(ngaykt);
        }
        if (!xuly.equals("")){
            theoXuly = new ArrayList<>();
            theoXuly = getDSXuLyByHinhThucXuLy(xuly);
        }
        return layPhanGiaoXL(theoNgayBD,theoNgayKT,theoXuly);
    }
}
