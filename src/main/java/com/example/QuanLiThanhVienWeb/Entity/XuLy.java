package com.example.QuanLiThanhVienWeb.Entity;

import jakarta.persistence.*;

@Entity(name = "Xuly")
@Table(name = "xuly")
public class XuLy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maXL;
    private int maTV;
    private String hinhThucXL;
    private Integer soTien;
    private String ngayXL;
    private int trangThaiXL;

    public XuLy( int maTV, String hinhThucXL, Integer soTien, String ngayXL) {
        this.maTV = maTV;
        this.hinhThucXL = hinhThucXL;
        this.soTien = soTien;
        this.ngayXL = ngayXL;
    }

    public XuLy() {
    }

    public int getMaXL() {
        return maXL;
    }

    public void setMaXL(int maXL) {
        this.maXL = maXL;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public String getHinhThucXL() {
        return hinhThucXL;
    }

    public void setHinhThucXL(String hinhThucXL) {
        this.hinhThucXL = hinhThucXL;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(Integer soTien) {
        this.soTien = soTien;
    }

    public String getNgayXL() {
        return ngayXL;
    }

    public void setNgayXL(String ngayXL) {
        this.ngayXL = ngayXL;
    }

    public int getTrangThaiXL() {
        return trangThaiXL;
    }

    public void setTrangThaiXL(int trangThaiXL) {
        this.trangThaiXL = trangThaiXL;
    }

    @Override
    public String toString() {
        return "XuLy{" +
                "maXL=" + maXL +
                ", maTV=" + maTV +
                ", hinhThucXL='" + hinhThucXL + '\'' +
                ", soTien=" + soTien +
                ", ngayXL='" + ngayXL + '\'' +
                ", trangThaiXL=" + trangThaiXL +
                '}';
    }
}

