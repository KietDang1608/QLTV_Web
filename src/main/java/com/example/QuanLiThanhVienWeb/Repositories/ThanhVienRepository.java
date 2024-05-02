ackage com.example.QuanLiThanhVienWeb.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.QuanLiThanhVienWeb.Entity.ThanhVien;

@Repository
public interface ThanhVienRepository extends CrudRepository<ThanhVien,Long> {
  Optional<ThanhVien> findById(long maTV);
}
