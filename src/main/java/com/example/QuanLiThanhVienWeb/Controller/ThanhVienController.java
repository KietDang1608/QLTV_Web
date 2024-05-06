package com.example.QuanLiThanhVienWeb.Controller;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import com.example.QuanLiThanhVienWeb.Repositories.ThietBiRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;
import com.example.QuanLiThanhVienWeb.Repositories.XuLyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ThanhVienController {
    @Autowired
    private ThanhVienRepository tvRe;
    @Autowired
    private XuLyRepository xlRe;


    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @GetMapping("/changePassword")
    public String toChangePassword(@RequestParam(name = "maTV") String maTV, Model model){
        model.addAttribute("maTV", maTV);
        return "doiMatKhau";
    }

    private static ArrayList<ThanhVien> thanhVienList = new ArrayList();
  
    @GetMapping("/QLThanhVien")
    public String getAll(Model m) {
        Iterable<ThanhVien> list = tvRe.findAll();
        thanhVienList = (ArrayList<ThanhVien>)tvRe.findAll();
        m.addAttribute("data",list);
        for (ThanhVien tv : list){
            System.out.println(tv.getHoTen());
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

    @PostMapping("/save-password")
    public String saveNewPass(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "maTV") long maTV,
            @RequestParam(name="oldPass") String oldPass,
            @RequestParam(name="password") String password,
            @RequestParam(name="confirmedPass") String confirmedPass
    ){
        try {
            Iterable<ThanhVien> list = tvRe.findAll();
            for (ThanhVien us : list){
                if(us.getMaTV() == maTV && us.getPassword().equals(oldPass)){
                    if (password.equals(confirmedPass)){
                        ThanhVien thanhVien = tvRe.findById(maTV).orElseThrow(null);
                        if (thanhVien == null){
                            System.out.println("Error not found user");
                            model.addAttribute("errorMessage", "Không tìm thấy thành viên!");
                            model.addAttribute("maTV", maTV);
                            String returnUrl = "/changePassword?maTV=" + maTV;
                            return "redirect:" + returnUrl;
                        }
                        thanhVien.setPassword(password);
                        tvRe.save(thanhVien);
                        model.addAttribute("errorMessage", "Đổi mật khẩu thành công!");
                        model.addAttribute("maTV", maTV);
                        String returnUrl = "/profile?maTV=" + maTV;
                        return "redirect:" + returnUrl;
                    }
                }
            }
            model.addAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            model.addAttribute("maTV", maTV);
            redirectAttributes.addAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            String returnUrl = "/changePassword?maTV=" + maTV;
            return "redirect:" + returnUrl;
        }
        catch (Exception e){
            System.out.println("Error "+e.getMessage());
            redirectAttributes.addAttribute("errorMessage", "Lỗi ThanhVienController");
            String returnUrl = "/changePassword?maTV=" + maTV;
            return "redirect:" + returnUrl;
        }
    }

    @GetMapping("/profile")
    public String getPersonalInfo(
            Model model,
            @RequestParam(name = "maTV") long maTV
    ){
        Optional<ThanhVien> data = tvRe.findById(maTV);
        List<XuLy> list = xlRe.findByMaTV(maTV);
        if (data.isPresent()){
            ThanhVien thanhVien = data.get();
            model.addAttribute("maTV", maTV);
            model.addAttribute("hoTen", thanhVien.getHoTen());
            model.addAttribute("khoa", thanhVien.getKhoa());
            model.addAttribute("nganh", thanhVien.getNganh());
            model.addAttribute("sdt", thanhVien.getSdt());
            model.addAttribute("email", thanhVien.getEmail());
            if (!list.isEmpty()){
                model.addAttribute("xuLyList", list);
            }
            return "profile";
        }
        return "home";
    }

    //---------------------------------------------------------------------------------
    @RequestMapping(value = "addTV")
    public String showAddForm(Model model) {
        ThanhVien tv = new ThanhVien();
        tv.setMaTV(getNewID());
        model.addAttribute("thanhvien", tv);
        return "addTV";
    }

    public int getNewID(){
        if(thanhVienList.isEmpty()) thanhVienList = (ArrayList<ThanhVien>) tvRe.findAll();
        int newID = thanhVienList.get(0).getMaTV();
        int i=0;
        while(newID!=thanhVienList.get(i).getMaTV()){
            newID++;
            i++;
        }//end
        return newID;
    }

    @PostMapping("/saveTV")
    public String save(@ModelAttribute("thanhvien") ThanhVien tv, Model model) {
        // Process the form data and save to the database
        String tentv = tv.getHoTen();
        String khoa = tv.getKhoa();
        String nganh = tv.getNganh();
        String sdt = tv.getSdt();
        String email = tv.getEmail();
        String password = tv.getPassword();

        // Create a new ThanhVien object with the data
        ThanhVien tvNew = new ThanhVien(this.getNewID(), tentv, khoa, nganh, sdt, email, password);
        tvRe.save(tvNew);

        // Retrieve all ThanhVien objects and add them to the model
        Iterable<ThanhVien> list = tvRe.findAll();
        model.addAttribute("list", list);

        // Redirect to the ThanhVien management page
        return "redirect:/QLThanhVien";
    }

    @GetMapping(value = {"QLThanhVien/edit/{id}"})
    public String showEditForm(@PathVariable("id") int id, Model model) {
        ThanhVien tv = thanhVienList.get(id);
        model.addAttribute("thanhvien", tv);
        return "editTV";
    }

    public ThanhVien getTVByID(int id){
        for(ThanhVien tv : tvRe.findAll()){
            if(tv.getMaTV() == id)
                return tv;
        }
        return null;
    }
    public int getTVIndex(int id){
        int i=0;
        for(;i<thanhVienList.size();i++)
            if(thanhVienList.get(i).getMaTV() == id) return i;
        return -1;
    }
    
    @PostMapping("/updateTV")
    public String updateTV(@ModelAttribute("thanhvien") ThanhVien tvNew) {
        // Retrieve existing ThanhVien object from the database
        Optional<ThanhVien> optionalTv = tvRe.findById((long)tvNew.getMaTV());
        
        if (optionalTv.isPresent()) {
            ThanhVien tv = optionalTv.get();
            // Update properties of the existing ThanhVien object
            tv.setHoTen(tvNew.getHoTen());
            tv.setKhoa(tvNew.getKhoa());
            tv.setNganh(tvNew.getNganh());
            tv.setSdt(tvNew.getSdt());
            tv.setEmail(tvNew.getEmail());
            tv.setPassword(tvNew.getPassword());
            // Save the updated ThanhVien object back to the database
            tvRe.save(tv);
        }
        
        return "redirect:/QLThanhVien";
    }

    public boolean checkXuLy(int id){
        Iterable<XuLy> list = xlRe.findAll();
        for(XuLy xl: list)
            if(xl.getMaTV() == id) return true;
        return false;
    }

    @GetMapping(value = "QLThanhVien/deleteTV/{id}")
    public String delete(@PathVariable("id") int id, Model model){
        ThanhVien tv = thanhVienList.get(id);
        if(tv!=null) tvRe.delete(tv);
        else if(checkXuLy(tv.getMaTV())) System.out.println("This member is being warned, deleting is unavailable!");
        else System.out.println("Can't find this user.");

        return "redirect:/QLThanhVien";
    }
    @PostMapping(value = "/QLThanhVien/deleteSelectedTV")
    public String deleteSelectedTV(@RequestParam("selectedItems") String selectedItems){
        String[] maTVs = selectedItems.split(",");
        for (String maTV : maTVs) {
            int id = Integer.parseInt(maTV);
            System.out.println(id);
            if(checkXuLy(id)==false){
                ThanhVien tv = getTVByID(id);
                tvRe.delete(tv);
            } else System.out.println("This member is being warned, deleting is unavailable!");
        }
        return "redirect:/QLThanhVien";
    }
    @PostMapping("/QLThanhVien/searchTV")
    public String searchTV(
        @RequestParam("searchBy") String type,
        @RequestParam("searchText") String searchText,
        Model model){
            ArrayList<ThanhVien> listFound = new ArrayList<>();
            switch(type){
                case "all":{
                    listFound = thanhVienList;
                    break;
                }
                case "maTV":{
                    if (!searchText.matches("\\d+")) break;
                    int maTV = Integer.parseInt(searchText);
                    ThanhVien tv = getTVByID(maTV);
                    if (tv != null) listFound.add(tv);
                }
                case "hoTen":{
                    for(ThanhVien tv : thanhVienList)
                        if(tv.getHoTen().contains(searchText)) listFound.add(tv);
                    break;
                }
                case "khoa":{
                    for(ThanhVien tv : thanhVienList)
                        if(tv.getKhoa().contains(searchText)) listFound.add(tv);
                    break;
                }
                case "nganh":
                    for(ThanhVien tv : thanhVienList){
                        if(tv.getNganh().contains(searchText)) listFound.add(tv);
                    break;
                }
                case "sdt":
                    for(ThanhVien tv : thanhVienList){
                        if(tv.getSdt().contains(searchText)) listFound.add(tv);
                    break;
                }
                case "email":{
                    for(ThanhVien tv : thanhVienList)
                        if(tv.getEmail().contains(searchText)) listFound.add(tv);
                    break;
                }
            }

            if(listFound.isEmpty()){
                model.addAttribute("message", "Không tìm thấy thành viên nào!");
            } else{
                model.addAttribute("data", listFound);
                for(ThanhVien tv : listFound){
                    model.addAttribute("maTV", tv.getMaTV());
                    model.addAttribute("hoTen", tv.getHoTen());
                    model.addAttribute("khoa", tv.getKhoa());
                    model.addAttribute("nganh", tv.getNganh());
                    model.addAttribute("sdt", tv.getSdt());
                    model.addAttribute("email", tv.getEmail());
                    model.addAttribute("password", tv.getPassword());
                }
            }


            return "ThanhVienView";
        }
}
