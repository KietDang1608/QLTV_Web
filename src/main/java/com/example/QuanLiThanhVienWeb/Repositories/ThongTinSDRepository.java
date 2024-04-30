package com.example.QuanLiThanhVienWeb.Repositories;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD,Integer> {
    //kiểm tra maTB có trong bảnmuwomuon hay chua
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM ThongTinSD t WHERE t.maTB = ?1")
    public Boolean existsByMaTB(Integer maTB);

    @Query("SELECT CASE WHEN COUNT(ttsd) > 0 THEN true ELSE false END " +
            "FROM ThongTinSD ttsd " +
            "WHERE ttsd.maTB = ?1 AND SUBSTRING(ttsd.tgDatcho, 1, 10) = FUNCTION('DATE_FORMAT', CURRENT_DATE(), '%Y-%m-%d')")
    boolean existsByMaTBAndNgayDatCho(Integer maTB);

    @Query("SELECT ttsd FROM ThongTinSD ttsd WHERE ttsd.maTV = ?1 and ttsd.tgDatcho is not null")
    public Boolean findByTGMuonIsNotNull(Integer maTV);
}
