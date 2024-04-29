package com.example.QuanLiThanhVienWeb.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "ThongTinSD")
@Table(name = "thongtinsd")
public class ThongTinSD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maTT;
    private int maTV;
    private Integer maTB;
    private String tgVao;
    private String tgMuon;
    private String tgTra;
    private String tgDatcho;

    public ThongTinSD(int maTV, Integer maTB, String tgVao, String tgMuon, String tgTra,String tgDatcho) {
        this.maTV = maTV;
        this.maTB = maTB;
        this.tgVao = tgVao;
        this.tgMuon = tgMuon;
        this.tgTra = tgTra;
        this.tgDatcho=tgDatcho;
    }

    public ThongTinSD() {
    }

    public int getMaTT() {
        return maTT;
    }

    public void setMaTT(int maTT) {
        this.maTT = maTT;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public int getMaTB() {
        return maTB != null ? maTB : -1;
    }

    public void setMaTB(Integer maTB) {
        this.maTB = maTB;
    }

    public String getTgVao() {
        return tgVao;
    }

    public void setTgVao(String tgVao) {
        this.tgVao = tgVao;
    }

    public String getTgMuon() {
        return tgMuon;
    }

    public void setTgMuon(String tgMuon) {
        this.tgMuon = tgMuon;
    }

    public String getTgTra() {
        return tgTra;
    }

    public void setTgTra(String tgTra) {
        this.tgTra = tgTra;
    }

    public String getTGDatcho()
    {
        return tgDatcho;
    }

    public void setTGDatcho(String tgDatcho)
    {
        this.tgDatcho=tgDatcho;
    }

    @Override
    public String toString() {
        return "ThongTinSD{" +
                "maTT=" + maTT +
                ", maTV=" + maTV +
                ", maTB=" + maTB +
                ", tgVao='" + tgVao + '\'' +
                ", tgMuon='" + tgMuon + '\'' +
                ", tgTra='" + tgTra + '\'' +
                ", tgDatcho='" + tgDatcho + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThongTinSD that = (ThongTinSD) o;
        return maTT == that.maTT && maTV == that.maTV && Objects.equals(maTB, that.maTB) && Objects.equals(tgVao, that.tgVao) && Objects.equals(tgMuon, that.tgMuon) && Objects.equals(tgTra, that.tgTra) && Objects.equals(tgDatcho, that.tgDatcho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maTT, maTV, maTB, tgVao, tgMuon, tgTra, tgDatcho);
    }
}

