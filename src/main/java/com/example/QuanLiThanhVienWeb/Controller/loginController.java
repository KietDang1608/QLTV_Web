package com.example.QuanLiThanhVienWeb.Controller;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;

import com.example.QuanLiThanhVienWeb.Repositories.ThanhVienRepository;




//import com.example.QuanLiThanhVienWeb.Repositories.userRepositoryImplement;

@Controller
public class loginController {
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private ThanhVienRepository tvRe;


	@Autowired
	private JavaMailSender mailSender;

  private ThanhVien tv;
    
	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}
	@RequestMapping("/logOut")
	public String logOut() {
		return "login";
	}
	
    @PostMapping("checklogin")
    public String checklogin(Model m,@RequestParam("taiKhoan")String taiKhoan, @RequestParam("password")String password) {
		try {
			if(taiKhoan.equals("") && password.equals("")) {
				m.addAttribute("erroll", "Tài khoản và mật khẩu không được để trống");
				return "login";
			}else {
				Iterable<ThanhVien> list = tvRe.findAll();
				for (ThanhVien us : list){
					if(us.getEmail().equals("admin") && us.getPassword().equals(password)) {
						return "admin";
					}else
					if(us.getEmail().equals(taiKhoan) && us.getPassword().equals(password)) {
						System.out.println("Đăng nhập thành công !!");
						m.addAttribute("username", us.getHoTen());
						m.addAttribute("maTV", us.getMaTV());
						return "home";
					}
				}
				m.addAttribute("erroll", "Sai Tài khoản hoặc mật khẩu");
				return "login";
			}
		}
		catch (Exception exception){
			m.addAttribute("erroll", exception.getMessage());
			return "login";
		}
    }
	@RequestMapping("/dangKy")
	public String showDangKy() {
		return "dangKy";
	}

	@PostMapping("checkdangKy") // Ensure this path matches the form action
	public String checkdangKy(Model m,
	                     @RequestParam("hoVaTen") String hoVaTen,
	                     @RequestParam("Khoa") String khoa,
	                     @RequestParam("Nganh") String nganh,
	                     @RequestParam("SDT") String sdt,
	                     @RequestParam("password") String password,
	                     @RequestParam("taiKhoan") String taiKhoan,
	                     @RequestParam("NMK") String nmk) {
	    if(taiKhoan.equals(" ")|| hoVaTen.equals(" ")||khoa.equals(" ")|| nganh.equals(" ")||sdt.equals(" ")|| password.equals(hoVaTen)||nmk.equals(" ")) {
	    	System.out.println("Đăng Ký thất bại !!");
	    	m.addAttribute("errollall", "Không được để trống thông tin !!");
	    	return "dangKy";
	    }if(!isValidEmail(taiKhoan)){
	    	System.out.println("Không phải Email !!");
	    	m.addAttribute("errollall", "Không đúng định dạng Email !!");
	    	return "dangKy";
	    }else if (!isValidPhoneNumber(sdt)) {
	    	System.out.println("Không phải số điện thoại !!");
	    	m.addAttribute("errollall", "Không đúng định dạng số điện thoại !!");
            return "dangKy";
        }else if (!password.equals(nmk)) {
            System.out.println("Mật khẩu không khớp!");
        	m.addAttribute("errollall", "Mật khẩu và nhập lại mật khẩu không khớp! !!");
            return "dangKy";
      
        }else {
	    	Iterable<ThanhVien> list = tvRe.findAll();
	        for (ThanhVien us : list){
		        	if(us.getEmail().equals(taiKhoan)){
		        		System.out.println("Tên email đã được đăng ký !! ");
		        		m.addAttribute("errollall", "Tên email đã được đăng ký !! ");
		        		return "dangKy";
		        	}
	        }
        	tv = new ThanhVien(hoVaTen,khoa,nganh,sdt,password,taiKhoan);
        	tvRe.save(tv);
        	m.addAttribute("success", "Đăng ký thành công");
        	return "login"; 
        }
	}
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
	private boolean isValidPhoneNumber(String phoneNumber) {
        Matcher matcher = PHONE_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
	@RequestMapping("/quenMK")
	public String quenMK() {
		return "quenMK";
	}
    @PostMapping("checkGmailvaSDT")
    public String checkGmailvaSDT(Model m,@RequestParam("taiKhoan")String taiKhoan, @RequestParam("SDT")String SDT) {
    	Iterable<ThanhVien> list = tvRe.findAll();
        for (ThanhVien us : list){
	        if(us.getEmail().equals(taiKhoan)) {
	        		if(us.getSdt().equals(SDT)) {
		        		System.out.println("Xác nhận thành công !!");  
		        		m.addAttribute("id", us.getMaTV());
		        		return "thayDoiMatKhau";
	        		}
	        		m.addAttribute("erroll", "Số điện thoại không đúng !!");
//	        		System.out.println("");
	        		return "quenMK";
	        	}
        }
        m.addAttribute("erroll", "Email không tồn tại !!");
    	System.out.println("Xác nhận thất bại !!");
        return "quenMK";
    }
    @PostMapping("thayDoiMatKhau")
    public String thayDoiMatKhau(Model m, @RequestParam("password") String password, @RequestParam("NMK") String NMK, @RequestParam("id") int id) {
    	if(password.isEmpty() || NMK.isEmpty()) {
    	    m.addAttribute("erroll", "Không được để trống ,xin vui lòng gửi lại !! ");
    	    return "quenMK";
    	}else 
        if (!password.equals(NMK)) {
            System.out.println("Mật khẩu không khớp!");
            m.addAttribute("erroll", "Mật khẩu không khớp , xin vui lòng gửi lại ");
            return "quenMK";
        } else {
            Optional<ThanhVien> optionalThanhVien = tvRe.findById((long) id);
            if (optionalThanhVien.isPresent()) {
                ThanhVien thanhVien = optionalThanhVien.get();
                thanhVien.setPassword(password);
                tvRe.save(thanhVien); // Lưu lại thay đổi vào cơ sở dữ liệu
                return "login"; // or redirect to another page
            } else {
                System.out.println("Không tìm thấy thành viên với id = " + id);
                return "quenMK";
            }
        }
    }


	//Forgot pass
	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		return "forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		String email = request.getParameter("email");
		String newPassword = UUID.randomUUID().toString().substring(0, 11);;
		System.out.println("newPassword: " + newPassword);
		ThanhVien tv = tvRe.findByEmail(email);
		tv.setPassword(newPassword);
		tvRe.save(tv);
		sendEmail(email, newPassword);
		model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		return "forgot_password_form";
	}

	public void sendEmail(String recipientEmail, String newPassWord) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		try {
			helper.setFrom("haha19042001@gmail.com", "Your Name"); // Thay "Your Name" bằng tên của bạn
			helper.setTo(recipientEmail);
			helper.setSubject("Here's the link to reset your password");

			String htmlBody = "<p>Hello,</p>"
					+ "<p>You have requested to reset your password.</p>"
					+ "<p>Here is your new password:</p>"
					+ "<p><strong>" + newPassWord + "</strong></p>"
					+ "<p>Ignore this email if you do remember your password, "
					+ "or you have not made the request.</p>";

			helper.setText(htmlBody, true); // Đặt tham số thứ hai thành true để xác định là HTML

			mailSender.send(message);
			System.out.println("Email sent successfully!");
		} catch (MessagingException | MailException e) {
			System.out.println("Failed to send email: " + e.getMessage());
		}
	}
}
