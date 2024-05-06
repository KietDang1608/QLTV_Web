package com.example.QuanLiThanhVienWeb.Repositories;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD,Integer> {
    @Query("SELECT t FROM ThongTinSD t WHERE FUNCTION('TIME_FORMAT', SUBSTRING(t.tgDatcho, 12, 8), '%H:%i:%s') < :oneHourAgo AND FUNCTION('DATE_FORMAT', SUBSTRING(t.tgDatcho, 1, 10), '%Y-%m-%d') = CURRENT_DATE() AND t.tgMuon is null")
    public ArrayList<ThongTinSD> findDatChoAfter1Hour(String oneHourAgo);
  
    List<ThongTinSD> findByMaTVAndMaTBIsNull(int maTV);

    List<ThongTinSD> findByMaTVAndMaTBIsNotNull(int maTV);
}
