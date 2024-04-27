package com.example.QuanLiThanhVienWeb.Entity;

import jakarta.persistence.*;

@Entity(name = "ThietBi")
@Table(name = "thietbi")
public class ThietBi {
    @Id
    @Column(name = "MaTB")
    private int maTB;

    @Column(name = "TenTB")
    private String tenTB;
    @Column(name = "MoTaTB")
    private String moTa;

    public ThietBi(String tenTB, String moTa) {
        this.tenTB = tenTB;
        this.moTa = moTa;
    }

    public ThietBi() {
    }

    public int getMaTB() {
        return maTB;
    }

    public void setMaTB(int maTB) {
        this.maTB = maTB;
    }

    public String getTenTB() {
        return tenTB;
    }

    public void setTenTB(String tenTB) {
        this.tenTB = tenTB;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "ThietBi{" +
                "maTB=" + maTB +
                ", tenTB='" + tenTB + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}

