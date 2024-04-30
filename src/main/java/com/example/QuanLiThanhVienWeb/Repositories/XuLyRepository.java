package com.example.QuanLiThanhVienWeb.Repositories;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import com.example.QuanLiThanhVienWeb.Entity.XuLy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuLyRepository extends CrudRepository<XuLy,Integer> {
}
