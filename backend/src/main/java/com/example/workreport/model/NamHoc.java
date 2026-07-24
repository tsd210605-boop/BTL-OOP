package com.example.workreport.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "NAM_HOC")
public class NamHoc {

    @Id
    @Column(name = "NamHocID")
    private String namHocID;

    @Column(name = "TenNamHoc")
    private String tenNamHoc;

    @OneToMany(mappedBy = "namHoc")
    private List<HocKy> hocKyList;

    public String getNamHocID() {
        return namHocID;
    }
    public void setNamHocID(String namHocID) {
        this.namHocID = namHocID;
    }
    public String getTenNamHoc() {
        return tenNamHoc;
    }
    public void setTenNamHoc(String tenNamHoc) {
        this.tenNamHoc = tenNamHoc;
    }
    public List<HocKy> getHocKyList() {
        return hocKyList;
    }
    public void setHocKyList(List<HocKy> hocKyList) {
        this.hocKyList = hocKyList;
    }
}
