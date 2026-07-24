package com.example.workreport.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "MON_HOC")
public class MonHoc {

    @Id
    @Column(name = "MonHocID", length = 50)
    private String monHocID;

    @Column(name = "TenMonHoc", length = 100)
    private String tenMonHoc;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "SoTinChi")
    private Integer soTinChi;

    @OneToMany(mappedBy = "monHoc")
    @JsonIgnore
    private List<LopHoc> lopHocList;

    public MonHoc() {}

    public MonHoc(String monHocID, String tenMonHoc, String moTa, Integer soTinChi) {
        this.monHocID = monHocID;
        this.tenMonHoc = tenMonHoc;
        this.moTa = moTa;
        this.soTinChi = soTinChi;
    }

    public String getMonHocID() {return monHocID;}
    public void setMonHocID(String monHocID) {this.monHocID = monHocID;}
    public String getTenMonHoc() {return tenMonHoc;}
    public void setTenMonHoc(String tenMonHoc) {this.tenMonHoc = tenMonHoc;}
    public String getMoTa() {return moTa;}
    public void setMoTa(String moTa) {this.moTa = moTa;}
    public Integer getSoTinChi() {return soTinChi;}
    public void setSoTinChi(Integer soTinChi) {this.soTinChi = soTinChi;}
    public List<LopHoc> getLopHocList() {return lopHocList;}
    public void setLopHocList(List<LopHoc> lopHocList) {this.lopHocList = lopHocList;}
}
