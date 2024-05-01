package com.example.QuanLiThanhVienWeb.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import com.example.QuanLiThanhVienWeb.Repositories.ThongTinSDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ScheduledTasks {

    @Autowired
    private ThongTinSDRepository ttsdRepository;
    private static ArrayList<ThongTinSD> listTTSD = new ArrayList();
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void findDatChoAfter1HourSerVice() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Trừ đi 1 giờ
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date oneHourAgo = calendar.getTime();

        // Định dạng thời gian
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // Chuyển đổi thời gian thành chuỗi theo định dạng "%H:%i:%s"
        String formattedOneHourAgo = formatter.format(oneHourAgo);

        listTTSD = ttsdRepository.findDatChoAfter1Hour(formattedOneHourAgo);
        System.out.println("Cac thiet bi dat cho qua 1 tieng");
        System.out.println(listTTSD);
        ttsdRepository.deleteAll(listTTSD);
    }
}

