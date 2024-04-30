package com.example.QuanLiThanhVienWeb.Repositories;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.ThongTinSD;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD, Integer> {
    List<ThongTinSD> findByMaTVAndMaTBIsNull(int maTV);

    List<ThongTinSD> findByMaTVAndMaTBIsNotNull(int maTV);
}
