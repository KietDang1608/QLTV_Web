package com.example.QuanLiThanhVienWeb.Repositories;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhVienRepository extends CrudRepository<ThanhVien,Long> {
}
