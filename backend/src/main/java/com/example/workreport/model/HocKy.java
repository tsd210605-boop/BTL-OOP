package com.example.workreport.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "HOC_KY")
public class HocKy {

    @Id
    @Column(name = "HocKyID")
    private String hocKyID;

    @Column(name = "TenHocKy")
    private String tenHocKy;

    @ManyToOne
    @JoinColumn(name = "NamHocID")
    private NamHoc namHoc;

    @OneToMany(mappedBy = "hocKy")
    private List<LopHoc> lopHocList;

    public String getHocKyID() {
        return hocKyID;
    }
    public void setHocKyID(String hocKyID) {
        this.hocKyID = hocKyID;
    }
    public String getTenHocKy() {
        return tenHocKy;
    }
    public void setTenHocKy(String tenHocKy) {
        this.tenHocKy = tenHocKy;
    }
    public NamHoc getNamHoc() {
        return namHoc;
    }
    public void setNamHoc(NamHoc namHoc) {
        this.namHoc = namHoc;
    }
    public List<LopHoc> getLopHocList() {
        return lopHocList;
    }
    public void setLopHocList(List<LopHoc> lopHocList) {
        this.lopHocList = lopHocList;
    }
}
