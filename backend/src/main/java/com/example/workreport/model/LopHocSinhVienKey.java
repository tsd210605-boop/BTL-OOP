package com.example.workreport.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LopHocSinhVienKey implements Serializable {
    private String lopID;
    private String sinhVienID;
    public LopHocSinhVienKey() {}

    public LopHocSinhVienKey(String lopID, String sinhVienID) {
        this.lopID = lopID;
        this.sinhVienID = sinhVienID;
    }

    public String getLopID() {
        return lopID;
    }
    public void setLopID(String lopID) {
        this.lopID = lopID;
    }
    public String getSinhVienID() {
        return sinhVienID;
    }
    public void setSinhVienID(String sinhVienID) {
        this.sinhVienID = sinhVienID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LopHocSinhVienKey)) return false;
        LopHocSinhVienKey that = (LopHocSinhVienKey) o;
        return Objects.equals(lopID, that.lopID) && Objects.equals(sinhVienID, that.sinhVienID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lopID, sinhVienID);
    }
}
